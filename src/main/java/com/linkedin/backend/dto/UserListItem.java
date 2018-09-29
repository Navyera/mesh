package com.linkedin.backend.dto;

import com.linkedin.backend.user.dao.AppUser;
import org.springframework.security.core.userdetails.User;

public class UserListItem {
    final private static UserListItem dummy = new UserListItem();

    private Integer userID;

    private String firstName;
    private String lastName;

    public UserListItem() {
    }

    public UserListItem(AppUser user) {
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

    public static UserListItem getDummy() {
        return dummy;
    }
}
