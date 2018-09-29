package com.linkedin.backend.handlers.advice;

import com.linkedin.backend.handlers.exception.ConversationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ConversationAdvice {
    @ResponseBody
    @ExceptionHandler(ConversationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String conversationNotFound(ConversationNotFoundException ex) {
        return ex.getMessage();
    }
}
