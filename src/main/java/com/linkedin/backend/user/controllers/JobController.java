package com.linkedin.backend.user.controllers;

import com.linkedin.backend.dto.JobDTO;
import com.linkedin.backend.dto.JobStatsDTO;
import com.linkedin.backend.dto.UserListItem;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.JobService;
import com.linkedin.backend.user.SkillService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.dao.Job;
import com.linkedin.backend.user.dao.Skill;
import com.linkedin.backend.user.handlers.JobNotFoundException;
import com.linkedin.backend.user.handlers.PostNotFoundException;
import com.linkedin.backend.user.handlers.SelfApplyException;
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
    public JobDTO createJob(@Valid @RequestHeader(value="Authorization") String auth, @Valid @RequestBody JobDTO jobDto)
                                                                                                   throws UserNotFoundException{
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        Job createdJob = jobService.createJob(user.getId(), jobDto);

        return new JobDTO(createdJob, user);
    }

    @GetMapping("/my-jobs")
    public List<JobDTO> getMyJobs(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        return user.getMyCreatedJobs()
                .stream()
                .map(j -> new JobDTO(j, user))
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
                .map(j -> new JobDTO(j, user, true))
                .sorted(Comparator.comparing(JobDTO::getDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return feed;
    }

    @PostMapping("/toggle-apply/{jobId}")
    public List<UserListItem> toggleApply(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer jobId)
                                                                                      throws UserNotFoundException, JobNotFoundException, SelfApplyException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        Job job = jobService.findJobById(jobId);

        if (user.equals(job.getOwner()))
            throw new SelfApplyException();

        if (!job.getApplicants().removeIf(a -> a.equals(user)))
            job.getApplicants().add(user);

        job = jobService.addJob(job);

        if (user.equals(job.getOwner()))
            return job.getApplicants().stream().map(UserListItem::new).collect(Collectors.toList());
        else
            return job.getApplicants().stream().map(c -> UserListItem.getDummy()).collect(Collectors.toList());
    }

    @GetMapping("/stats")
    public JobStatsDTO getJobStats(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException, JobNotFoundException {
            JWTUtils token = new JWTUtils(auth);
            AppUser user = appUserService.findUserById(token.getUserID());

            List<Skill> trendingSkills = skillService.getTopNTrending(5);

            return new JobStatsDTO(user, trendingSkills);
    }
}
