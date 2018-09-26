package com.linkedin.backend.conversation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ConversationAdvice {
    @ResponseBody
    @ExceptionHandler(ConversationNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String conversationNotFound(ConversationNotFound ex) {
        return ex.getMessage();
    }
}
