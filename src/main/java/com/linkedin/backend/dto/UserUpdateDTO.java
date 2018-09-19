package com.linkedin.backend.dto;

public class UserUpdateDTO {
    // Used for user detail update
    private String firstName;
    private String lastName;
    private Integer phone;

    // Used for email update
    private String email;

    // Used for password update
    private String oldPassword;
    private String newPassword;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String firstName, String lastName, Integer phone, String email, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
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

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
