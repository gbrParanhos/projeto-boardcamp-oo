package com.boardcamp.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionsHandler {
  @ExceptionHandler({ CustomException.class })
  public ResponseEntity<String> handleCustomException(CustomException exception) {
    if (exception.getType().equals("conflict")) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
    if (exception.getType().equals("notFound")) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }
}
