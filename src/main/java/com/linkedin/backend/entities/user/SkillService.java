package com.linkedin.backend.entities.user;

import com.linkedin.backend.entities.user.dao.AppUser;
import com.linkedin.backend.entities.user.dao.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillService {
    private final UserRepository userRepository;
    private final SkillsRepository skillsRepository;

    @Autowired
    public SkillService(UserRepository userRepository, SkillsRepository skillsRepository) {
        this.userRepository = userRepository;
        this.skillsRepository = skillsRepository;
    }

    Skill findSkillByDesc(String desc) {
        return skillsRepository.findBySkillDescription(desc);
    }

    public List<Skill> generateSkills(List<String> skills) {
        List<Skill> newSkills = new ArrayList<>();

        if (skills == null)
            return newSkills;

        for (String skill : skills) {
            Skill newSkill = findSkillByDesc(skill);

            if (newSkill == null)
                newSkill = new Skill(skill);

            newSkills.add(newSkill);
        }

        return newSkills;
    }

    public void setSkillsFromStrList(AppUser user, List<String> skills) {
        List<Skill> newSkills = generateSkills(skills);

        user.setSkills(newSkills);

        userRepository.save(user);
    }

    public List<Skill> getTopNTrending(Integer n) {
        Iterable<Skill> skills = skillsRepository.findAll();

        Map<Skill, Integer> skillMap = new HashMap<>();

        for (Skill skill : skills)
            skillMap.put(skill, skill.getRelevantJobs().size());

        return skillMap
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(n)
                .collect(Collectors.toList());
    }
}
