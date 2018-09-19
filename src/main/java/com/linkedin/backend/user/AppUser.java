package com.linkedin.backend.user;

import com.linkedin.backend.models.RegisterModel;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private String role;

    public AppUser() {

    }

    public AppUser(String email, String password, String firstName, String lastName, String phone) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = "ROLE_USER";
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

    public RegisterModel toRegisterModel() {
        return new RegisterModel(this.firstName, this.lastName, this.phone,
                           "", this.email, "", "");
    }
}
