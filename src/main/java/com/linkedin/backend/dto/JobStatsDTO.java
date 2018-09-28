package com.linkedin.backend.dto;

import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.dao.Job;
import com.linkedin.backend.user.dao.Skill;

import javax.swing.text.StyledEditorKit;
import java.util.List;
import java.util.stream.Collectors;

public class JobStatsDTO {
    private String firstName;
    private String lastName;

    private String job;

    private List<String> trendingSkills;

    public JobStatsDTO(AppUser user, List<Skill> trendingSkills) {
        firstName = user.getFirstName();
        lastName = user.getLastName();

        job = user.getProfile().getJob();

        this.trendingSkills = trendingSkills.stream().map(Skill::getSkillDescription).collect(Collectors.toList());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<String> getTrendingSkills() {
        return trendingSkills;
    }

    public void setTrendingSkills(List<String> trendingSkills) {
        this.trendingSkills = trendingSkills;
    }
}
