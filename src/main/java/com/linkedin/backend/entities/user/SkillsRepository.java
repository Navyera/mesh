package com.linkedin.backend.entities.user;

import com.linkedin.backend.entities.user.dao.Skill;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface SkillsRepository extends CrudRepository<Skill, Integer> {
    Skill findBySkillDescription(String description);
}
