package com.linkedin.backend.entities.skill;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface SkillRepository extends CrudRepository<Skill, Integer> {
    Skill findBySkillDescription(String description);
}
