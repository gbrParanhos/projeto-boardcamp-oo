package com.boardcamp.api.exceptions;

public class CustomException extends RuntimeException {
  private final String type;

  public CustomException(String type, String message) {
    super(message);
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
