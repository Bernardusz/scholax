package io.github.bernardusz.scholax.exception.exceptions;

public class ClassroomNotFound extends RuntimeException {
  private final int errorCode;
  public ClassroomNotFound(String message) {
    super(message);
    this.errorCode = 404;
  }
  public int getErrorCode(){
    return errorCode;
  }
}
