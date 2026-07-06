package io.github.bernardusz.scholax.exception.exceptions;

public class AnnouncementNotFound extends RuntimeException {
  private final int errorCode;
  public AnnouncementNotFound(String message) {
    super(message);
    this.errorCode = 404;
  }
  public int getErrorCode(){
    return errorCode;
  }
}
