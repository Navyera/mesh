package com.linkedin.backend.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.linkedin.backend.entities.user.AppUser;
import com.linkedin.backend.entities.user.Profile;

import java.util.List;
import java.util.stream.Collectors;

public class AppUserXML {
    private Integer id;

    private String firstName;

    private String lastName;

    private String bio;

    private String education;

    private String job;

    @JacksonXmlElementWrapper(localName = "Skills")
    @JacksonXmlProperty(localName = "Skill")
    private List<SkillXML> skills;

    @JacksonXmlElementWrapper(localName = "Posts")
    @JacksonXmlProperty(localName = "Post")
    private List<PostXML> posts;

    @JacksonXmlElementWrapper(localName = "Likes")
    @JacksonXmlProperty(localName = "Like")
    private List<LikeXML> likes;

    @JacksonXmlElementWrapper(localName = "Comments")
    @JacksonXmlProperty(localName = "Comment")
    private List<CommentXML> comments;

    @JacksonXmlElementWrapper(localName = "Connections")
    @JacksonXmlProperty(localName = "Connection")
    private List<ConnectionXML> connections;

    public AppUserXML(AppUser user) {
        id = user.getId();

        firstName = user.getFirstName();
        lastName  = user.getLastName();

        Profile profile = user.getProfile();

        bio = profile.getAbout();
        education = profile.getEducation();
        job = profile.getJob();

        skills = user.getSkills().stream().map(SkillXML::new).collect(Collectors.toList());
        posts = user.getPosts().stream().map(PostXML::new).collect(Collectors.toList());
        likes = user.getLikes().stream().map(LikeXML::new).collect(Collectors.toList());
        comments = user.getComments().stream().map(CommentXML::new).collect(Collectors.toList());
        connections = user.getFriendIDs().stream().map(ConnectionXML::new).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public List<SkillXML> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillXML> skills) {
        this.skills = skills;
    }

    public List<PostXML> getPosts() {
        return posts;
    }

    public void setPosts(List<PostXML> posts) {
        this.posts = posts;
    }

    public List<LikeXML> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeXML> likes) {
        this.likes = likes;
    }

    public List<CommentXML> getComments() {
        return comments;
    }

    public void setComments(List<CommentXML> comments) {
        this.comments = comments;
    }

    public List<ConnectionXML> getConnections() {
        return connections;
    }

    public void setConnections(List<ConnectionXML> connections) {
        this.connections = connections;
    }
}
