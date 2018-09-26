package com.linkedin.backend.user.handlers;



import com.linkedin.backend.connection.NotFriendsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ConnectionAdvice {
    @ResponseBody
    @ExceptionHandler(DuplicateConnectionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String duplicateConnection(DuplicateConnectionException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ConnectionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String missingConnection(ConnectionNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotFriendsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String notFriends(NotFriendsException ex) {
        return ex.getMessage();
    }
}
