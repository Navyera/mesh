package com.linkedin.backend.dto;

public class UserDetailsDTO {
    // Used for user detail update
    private String firstName;
    private String lastName;
    private String phone;

    // Used for picture update
    private String picture;

    // Used for email update
    private String email;

    // Used for password update
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

    public UserDetailsDTO() {
    }

    public UserDetailsDTO(String firstName, String lastName, String phone, String picture, String email, String oldPassword, String newPassword, String confirmNewPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.picture = picture;
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public UserDetailsDTO(String firstName, String lastName, String phone, String picture, String email) {
        this(firstName, lastName, phone, picture, email, "", "", "");
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
