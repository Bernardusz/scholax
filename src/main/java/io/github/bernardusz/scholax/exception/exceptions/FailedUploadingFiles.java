package io.github.bernardusz.scholax.exception.exceptions;

public class FailedUploadingFiles extends RuntimeException {
  private final int errorCode;

  public FailedUploadingFiles(String message) {
    super(message);
    this.errorCode = 500;
  }

  public Integer getErrorCode(){
    return errorCode;
  }
}
