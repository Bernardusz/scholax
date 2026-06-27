package io.github.bernardusz.scholax.exception.exceptions;

public class FileNotFound extends RuntimeException {
  private final int errorCode;

  public FileNotFound(String message) {
    super(message);
    this.errorCode = 404;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
