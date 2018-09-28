package com.linkedin.backend.user;

import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.dao.Skill;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface SkillsRepository extends CrudRepository<Skill, Integer> {
    Skill findBySkillDescription(String description);
}
