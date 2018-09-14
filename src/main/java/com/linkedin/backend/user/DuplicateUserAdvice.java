package com.linkedin.backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class DuplicateUserAdvice {

    @ResponseBody
    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String duplicateUserHandler(DuplicateUserException ex) {
        return ex.getMessage();
    }
}
