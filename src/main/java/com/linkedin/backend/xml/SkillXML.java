package com.linkedin.backend.xml;

import com.linkedin.backend.entities.user.dao.Skill;

public class SkillXML {
    private Integer id;

    private String description;

    public SkillXML(Skill skill) {
        description = skill.getSkillDescription();
        id = skill.getSkillId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
