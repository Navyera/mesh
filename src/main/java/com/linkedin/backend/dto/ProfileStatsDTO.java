package com.linkedin.backend.dto;

import com.linkedin.backend.entities.user.dao.AppUser;

public class ProfileStatsDTO {
    private String firstName;
    private String lastName;

    private String job;

    private Integer connections;
    private Integer posts;

    public ProfileStatsDTO() {
    }

    public ProfileStatsDTO(AppUser user) {
        firstName = user.getFirstName();
        lastName  = user.getLastName();

        job = user.getProfile().getJob();

        connections = user.getFriendIDs().size();
        posts = user.getPosts().size();
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

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }
}
