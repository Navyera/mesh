package com.linkedin.backend.user;

import com.linkedin.backend.user.dao.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Integer> {
}
