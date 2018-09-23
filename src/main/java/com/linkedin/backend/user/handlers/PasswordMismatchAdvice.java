package com.linkedin.backend.user.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PasswordMismatchAdvice {
    @ResponseBody
    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String passwordMismatch(PasswordMismatchException ex) {
        return ex.getMessage();
    }
}
