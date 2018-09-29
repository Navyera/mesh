package com.linkedin.backend.entities.skill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkedin.backend.entities.user.AppUser;
import com.linkedin.backend.entities.job.Job;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer skillId;

    private String skillDescription;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<AppUser> users;

    @ManyToMany(mappedBy = "requiredSkills")
    private List<Job> relevantJobs;

    public Skill() {
    }

    public Skill(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public List<AppUser> getUsers() {
        return users;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    public List<Job> getRelevantJobs() {
        return relevantJobs;
    }

    public void setRelevantJobs(List<Job> relevantJobs) {
        this.relevantJobs = relevantJobs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Skill skill = (Skill) o;
        return Objects.equals(skillId, skill.skillId);
    }
}
