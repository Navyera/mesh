package com.linkedin.backend.dto;

import com.linkedin.backend.user.dao.Job;
import com.linkedin.backend.user.dao.Skill;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JobDTO {
    private Integer jobID;

    private String jobTitle;
    private String jobDescription;
    private List<String> requiredSkills;

    private List<UserListItem> applicants;

    private Integer ownerID;

    private Boolean fromFriend;

    public JobDTO() {
    }

    public JobDTO(Job job) {
        this(job, false);
    }

    public JobDTO(Job job, Boolean fromFriend) {
        jobID = job.getJobId();

        jobTitle = job.getJobTitle();
        jobDescription = job.getJobDescription();

        requiredSkills = job.getRequiredSkills().stream().map(Skill::getSkillDescription).collect(Collectors.toList());
        applicants = job.getApplicants().stream().map(UserListItem::new).collect(Collectors.toList());

        ownerID = job.getOwner().getId();

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

    public List<UserListItem> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<UserListItem> applicants) {
        this.applicants = applicants;
    }

    public Boolean getFromFriend() {
        return fromFriend;
    }

    public void setFromFriend(Boolean fromFriend) {
        this.fromFriend = fromFriend;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }
}
