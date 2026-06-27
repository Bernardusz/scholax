package io.github.bernardusz.scholax.exception.exceptions;

public class UserNotFound extends RuntimeException {
  private final int errorCode;

  public UserNotFound(String message) {
    super(message);
    this.errorCode = 404;
  }

  public int getErrorCode(){
    return errorCode;
  }
}
