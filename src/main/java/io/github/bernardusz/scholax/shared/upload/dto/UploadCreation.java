package io.github.bernardusz.scholax.shared.upload.dto;

public record UploadCreation(
  String fileName,
  String url,
  String googleFileId
) {}
