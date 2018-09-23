package com.linkedin.backend.connection;



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
}
