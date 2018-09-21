package com.linkedin.backend.dto;

import com.linkedin.backend.user.Skill;

import java.util.ArrayList;
import java.util.List;

public class ProfileDTO {
    private String firstName;
    private String lastName;

    private String about;
    private String education;
    private String job;

    private List<Skill> skills;

    public ProfileDTO(String firstName, String lastName, String about, String education, String job, List<Skill> skills) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.about = about;
        this.education = education;
        this.job = job;
        this.skills = skills;
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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
