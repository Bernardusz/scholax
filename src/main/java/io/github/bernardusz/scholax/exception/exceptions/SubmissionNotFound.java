package io.github.bernardusz.scholax.exception.exceptions;

public class SubmissionNotFound extends RuntimeException {
  private final int errorCode;
  public SubmissionNotFound(String message) {
    super(message);
    this.errorCode = 404;
  }
  public int getErrorCode(){
    return errorCode;
  }
}
