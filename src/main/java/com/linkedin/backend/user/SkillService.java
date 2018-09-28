package com.linkedin.backend.user;

import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.dao.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public void setSkillsFromStrList(AppUser user, List<String> skills) {
        List<Skill> newSkills = new ArrayList<>();

        for (String skill : skills) {
            Skill newSkill = findSkillByDesc(skill);

            if (newSkill == null)
                newSkill = new Skill(skill);

            newSkills.add(newSkill);
        }

        user.setSkills(newSkills);

        userRepository.save(user);
    }
}
