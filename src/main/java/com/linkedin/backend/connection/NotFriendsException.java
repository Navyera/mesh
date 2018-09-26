package com.linkedin.backend.connection;

public class NotFriendsException extends Exception {
    private Integer idA;
    private Integer idB;

    public NotFriendsException(Integer idA, Integer idB) {
        this.idA = idA;
        this.idB = idB;
    }

    @Override
    public String getMessage() {
        return "Users " + idA + " and " + idB + " are not friends";
    }
}
