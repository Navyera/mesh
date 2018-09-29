package com.linkedin.backend.dto;

import com.linkedin.backend.entities.user.AppUser;

public class UserListItemDTO {
    final private static UserListItemDTO dummy = new UserListItemDTO();

    private Integer userID;

    private String firstName;
    private String lastName;

    public UserListItemDTO() {
    }

    public UserListItemDTO(AppUser user) {
        userID = user.getId();

        firstName = user.getFirstName();
        lastName  = user.getLastName();
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public static UserListItemDTO getDummy() {
        return dummy;
    }
}
