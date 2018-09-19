package com.linkedin.backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class PasswordMismatchAdvice {
    @ResponseBody
    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String passwordMismatch(UserNotFoundException ex) {
        return ex.getMessage();
    }
}
