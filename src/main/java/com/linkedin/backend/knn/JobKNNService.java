package com.linkedin.backend.knn;

import com.linkedin.backend.entities.user.AppUserService;
import com.linkedin.backend.entities.skill.SkillRepository;
import com.linkedin.backend.entities.user.AppUser;
import com.linkedin.backend.entities.job.Job;
import com.linkedin.backend.entities.skill.Skill;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobKNNService {
    private final SkillRepository skillRepository;
    private final AppUserService appUserService;

    public class KNNResult {
        private AppUser user;
        private Double distance;

        public KNNResult(AppUser user, Double distance) {
            this.user = user;
            this.distance = distance;
        }

        public AppUser getUser() {
            return user;
        }

        public void setUser(AppUser user) {
            this.user = user;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }
    }

    public JobKNNService(SkillRepository skillRepository, AppUserService appUserService) {
        this.skillRepository = skillRepository;
        this.appUserService = appUserService;
    }

    private double distance(List<Double> myVector, List<Double> otherVector) {
        double distance = 0.0;

        for (int i = 0; i < myVector.size(); ++i)
            distance += Math.pow(myVector.get(i) - otherVector.get(i), 2);

        return Math.sqrt(distance);
    }

    private List<Double> createVector(AppUser user, Map<Skill, Integer> index) {
        List<Double> myVector = new ArrayList<>(Collections.nCopies(index.keySet().size(), 0.0));

        for (Skill skill : user.getSkills())
            myVector.set(index.get(skill), 1.0);

        return myVector;
    }

    private List<KNNResult> getUserKNN(AppUser user, Integer k) {
        // Map posts, to make post -> vector index mapping.
        Map<Skill, Integer> skillIndices = new HashMap<>();

        PriorityQueue<KNNResult> pqueue = new PriorityQueue<>(Comparator.comparing(KNNResult::getDistance));

        int idx = 0;
        for (Skill skill : skillRepository.findAll())
            skillIndices.put(skill, idx++);

        List<Double> myVector = createVector(user, skillIndices);

        double maxDistance = 0.0;

        // Iterate for every user other than me and my friends.
        for (AppUser other : appUserService.findAll()) {
            if (other.equals(user))
                continue;

            List<Double> otherVector = createVector(other, skillIndices);

            double distance = distance(myVector, otherVector);

            maxDistance = distance > maxDistance ? distance : maxDistance;

            pqueue.add(new KNNResult(other, distance));
        }

        if (maxDistance == 0.0)
            maxDistance = 1.0;

        final double normalizer = maxDistance;

        return pqueue
                .stream()
                .sorted(Comparator.comparing(KNNResult::getDistance))
                .map(e -> new KNNResult(e.user, e.distance/normalizer))
                .limit(k)
                .collect(Collectors.toList());
    }

    public List<Job> generateUserKNN(AppUser user) {
        List<KNNResult> knn = getUserKNN(user, 5);

        List<Job> similarJobs = knn.stream()
                                    .map(KNNResult::getUser)
                                    .map(AppUser::getMyAppliedJobs)
                                    .flatMap(List::stream)
                                    .collect(Collectors.toList());

        return similarJobs;
    }
}
