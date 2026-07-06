package io.github.bernardusz.scholax.exception.exceptions;

public class AssignmentNotFound extends RuntimeException {
  private final int errorCode;
  public AssignmentNotFound(String message) {
    super(message);
    this.errorCode = 404;
  }
  public int getErrorCode(){
    return errorCode;
  }
}
