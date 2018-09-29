package com.linkedin.backend.entities.user;

import com.linkedin.backend.entities.user.dao.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Integer> {
}
