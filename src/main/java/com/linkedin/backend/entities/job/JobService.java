package com.linkedin.backend.entities.job;

import com.linkedin.backend.dto.JobDTO;
import com.linkedin.backend.entities.skill.SkillService;
import com.linkedin.backend.entities.user.AppUserService;
import com.linkedin.backend.entities.user.AppUser;
import com.linkedin.backend.handlers.exception.JobNotFoundException;
import com.linkedin.backend.handlers.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final AppUserService appUserService;
    private final SkillService skillService;

    public JobService(JobRepository jobRepository, AppUserService appUserService, SkillService skillService) {
        this.jobRepository = jobRepository;
        this.appUserService = appUserService;
        this.skillService = skillService;
    }

    public Job createJob(Integer userId, JobDTO jobDto) throws UserNotFoundException{
        AppUser owner = appUserService.findUserById(userId);

        Job job = new Job();

        job.setOwner(owner);
        job.setJobTitle(jobDto.getJobTitle());
        job.setJobDescription(jobDto.getJobDescription());

        // TODO: Set applicants? YES
        job.setApplicants(Collections.emptyList());

        job.setRequiredSkills(skillService.generateSkills(jobDto.getRequiredSkills()));

        return jobRepository.save(job);
    }

    public Job addJob(Job job) {
        return jobRepository.save(job);
    }

    public Job findJobById(Integer jobId) throws JobNotFoundException {
        return jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException(jobId));
    }
}
