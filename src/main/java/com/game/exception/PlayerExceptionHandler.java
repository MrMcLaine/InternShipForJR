package com.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PlayerExceptionHandler {
    @ExceptionHandler(CustomExceptionBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void sendIncorrectParams(CustomExceptionBadRequest e) {
    }

    @ExceptionHandler(CustomExceptionNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFoundPlayer(CustomExceptionNotFound e) {
    }


}
