package com.linkedin.backend.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.linkedin.backend.dto.ProfileDTO;
import com.linkedin.backend.dto.UserDetailsDTO;
import com.linkedin.backend.models.RegisterModel;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class AppUser implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private String role;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private Profile profile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private List<Skill> skills;

    public AppUser() {
    }

    public AppUser(Profile profile) {
        this.profile = profile;
        profile.setUser(this);
    }

    public AppUser(String email, String password, String firstName, String lastName, String phone) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = "ROLE_USER";
        this.profile = new Profile();
        profile.setUser(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<Role> getAuthority() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new Role(role));
        return roles;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkillsFromStrList(List<String> skills) {
        this.skills = skills.stream().map(skill -> new Skill(skill, this)).collect(Collectors.toList());
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public UserDetailsDTO toUserDetails() {
        return new UserDetailsDTO(this.firstName, this.lastName, this.phone,"", this.email);
    }

    public ProfileDTO toProfileDTO() {
        return new ProfileDTO(firstName, lastName, profile.getAbout(), profile.getEducation(), profile.getJob(), skills);
    }
}
