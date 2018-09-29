package com.linkedin.backend.entities.skill;

import com.linkedin.backend.entities.user.UserRepository;
import com.linkedin.backend.entities.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public SkillService(UserRepository userRepository, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    Skill findSkillByDesc(String desc) {
        return skillRepository.findBySkillDescription(desc);
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
        Iterable<Skill> skills = skillRepository.findAll();

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
