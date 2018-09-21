package com.linkedin.backend.user;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface SkillsRepository extends CrudRepository<Skill, Integer> {
    void deleteSkillsByUser(AppUser user);
}
