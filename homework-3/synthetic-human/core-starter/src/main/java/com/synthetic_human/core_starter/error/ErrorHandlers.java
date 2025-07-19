package com.synthetic_human.core_starter.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.RejectedExecutionException;

@ControllerAdvice
public class ErrorHandlers {

    @ExceptionHandler(CommandValidationException.class)
    private ResponseEntity<String> commandValidationError(CommandValidationException exception) {
        return ResponseEntity.badRequest().body(String.format("Command validation error: %s", exception.getMessage()));
    }

    @ExceptionHandler(RejectedExecutionException.class)
    private ResponseEntity<String> queueOverflowError(RejectedExecutionException exception) {
        return ResponseEntity.badRequest().body(String.format("Reject execution error: %s", exception.getMessage()));

    }

}
