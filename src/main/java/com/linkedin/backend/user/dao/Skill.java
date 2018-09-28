package com.linkedin.backend.user.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

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
}
