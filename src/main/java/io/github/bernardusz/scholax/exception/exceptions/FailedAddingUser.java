package io.github.bernardusz.scholax.exception.exceptions;

public class FailedAddingUser extends RuntimeException {
  private final int errorCode;
  public FailedAddingUser(String message) {
    super(message);
    this.errorCode = 500;
  }

  public int getErrorCode(){
    return errorCode;
  }
}
