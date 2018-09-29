package com.linkedin.backend.entities.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.linkedin.backend.storage.File;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Profile implements Serializable{
    @Id
    @OneToOne
    @JoinColumn(name = "id")
    @JsonBackReference
    private AppUser user;

    private String about;

    private String job;

    private String education;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_picture")
    @JsonManagedReference
    private File profilePicture;

    private int permissions = 0;

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public File getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(File profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }
}
