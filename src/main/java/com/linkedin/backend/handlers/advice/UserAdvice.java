package com.linkedin.backend.handlers.advice;

import com.linkedin.backend.handlers.exception.DuplicateUserException;
import com.linkedin.backend.handlers.exception.PasswordMismatchException;
import com.linkedin.backend.handlers.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserAdvice {
    @ResponseBody
    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String duplicateUserHandler(DuplicateUserException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String passwordMismatchHandler(PasswordMismatchException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }
}
