package com.linkedin.backend.user.controllers;

import com.linkedin.backend.dto.JobDTO;
import com.linkedin.backend.dto.JobStatsDTO;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.JobService;
import com.linkedin.backend.user.SkillService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.dao.Job;
import com.linkedin.backend.user.dao.Skill;
import com.linkedin.backend.user.handlers.JobNotFoundException;
import com.linkedin.backend.user.handlers.PostNotFoundException;
import com.linkedin.backend.user.handlers.UserNotFoundException;
import com.linkedin.backend.utils.JSONReturn;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/jobs")
public class JobController {
    final private AppUserService appUserService;
    final private SkillService skillService;
    final private JobService jobService;

    public JobController(AppUserService appUserService, JobService jobService, SkillService skillService) {
        this.appUserService = appUserService;
        this.jobService = jobService;
        this.skillService = skillService;
    }

    @PostMapping("")
    public JSONReturn<Integer> createJob(@Valid @RequestHeader(value="Authorization") String auth, @Valid @RequestBody JobDTO jobDto)
                                                                                                   throws UserNotFoundException{
        JWTUtils token = new JWTUtils(auth);
        Integer creatorId = token.getUserID();

        Job createdJob = jobService.createJob(creatorId, jobDto);

        return new JSONReturn<>(createdJob.getJobId());
    }

    @GetMapping("/my-jobs")
    public List<JobDTO> getMyJobs(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        return user.getMyCreatedJobs()
                .stream()
                .map(JobDTO::new)
                .sorted(Comparator.comparing(JobDTO::getDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @GetMapping("/feed")
    public List<JobDTO> getJobFeed(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        List<JobDTO> feed = user.getFriends()
                .stream()
                .map(AppUser::getMyCreatedJobs)
                .flatMap(List::stream)
                .map(j -> new JobDTO(j, true))
                .sorted(Comparator.comparing(JobDTO::getDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return feed;
    }

    @PostMapping("/toggle-apply/{jobId}")
    public JSONReturn<Boolean> toggleApply(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer jobId)
                                                                                      throws UserNotFoundException, JobNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        Job job = jobService.findJobById(jobId);

        Boolean ret = false;

        if (!job.getApplicants().removeIf(a -> a.equals(user))) {
            job.getApplicants().add(user);
            ret = true;
        }

        jobService.addJob(job);

        return new JSONReturn<>(ret);
    }

    @GetMapping("/stats")
    public JobStatsDTO getJobStats(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException, JobNotFoundException {
            JWTUtils token = new JWTUtils(auth);
            AppUser user = appUserService.findUserById(token.getUserID());

            List<Skill> trendingSkills = skillService.getTopNTrending(5);

            return new JobStatsDTO(user, trendingSkills);
    }
}
