package com.linkedin.backend.dto;

import com.linkedin.backend.entities.user.AppUser;
import com.linkedin.backend.entities.job.Job;
import com.linkedin.backend.entities.skill.Skill;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JobDTO {
    private Integer jobID;

    private String jobTitle;
    private String jobDescription;
    private List<String> requiredSkills;

    private List<UserListItemDTO> applicants;

    private Integer ownerID;

    private Boolean applied;

    private Date date;

    private Boolean fromFriend;

    public JobDTO() {
    }

    public JobDTO(Job job, AppUser user) {
        this(job, user, false);
    }

    public JobDTO(Job job, AppUser user, Boolean fromFriend) {
        jobID = job.getJobId();

        jobTitle = job.getJobTitle();
        jobDescription = job.getJobDescription();

        requiredSkills = job.getRequiredSkills().stream().map(Skill::getSkillDescription).collect(Collectors.toList());

        if (user.equals(job.getOwner()))
            applicants = job.getApplicants().stream().map(UserListItemDTO::new).collect(Collectors.toList());
        else
            applicants = job.getApplicants().stream().map(c -> UserListItemDTO.getDummy()).collect(Collectors.toList());

        ownerID = job.getOwner().getId();

        date = job.getDate();

        applied = user.getMyAppliedJobs().contains(job);

        this.fromFriend = fromFriend;
    }

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public List<UserListItemDTO> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<UserListItemDTO> applicants) {
        this.applicants = applicants;
    }

    public Boolean getFromFriend() {
        return fromFriend;
    }

    public void setFromFriend(Boolean fromFriend) {
        this.fromFriend = fromFriend;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public Boolean getApplied() {
        return applied;
    }

    public void setApplied(Boolean applied) {
        this.applied = applied;
    }
}
