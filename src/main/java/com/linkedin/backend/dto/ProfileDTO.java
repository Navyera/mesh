package com.linkedin.backend.dto;

import com.linkedin.backend.entities.user.AppUser;
import com.linkedin.backend.entities.user.Profile;
import com.linkedin.backend.entities.skill.Skill;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileDTO {
    private String firstName;
    private String lastName;

    private String about;
    private String education;
    private String job;

    private List<String> skills;

    public ProfileDTO() {
    }

    public ProfileDTO(AppUser user) {
        this(user, true);
    }

    public ProfileDTO(AppUser user, boolean light) {
        firstName = user.getFirstName();
        lastName  = user.getLastName();

        if (light)
            return;

        Profile profile = user.getProfile();

        about = profile.getAbout();
        job   = profile.getJob();
        education = profile.getEducation();

        skills = user.getSkills().stream().map(Skill::getSkillDescription).collect(Collectors.toList());
    }

    public ProfileDTO(String firstName, String lastName, String about, String education, String job, List<Skill> skills) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.about = about;
        this.education = education;
        this.job = job;
        this.skills = skills.stream().map(Skill::getSkillDescription).collect(Collectors.toList());
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
