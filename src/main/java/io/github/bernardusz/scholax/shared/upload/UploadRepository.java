package io.github.bernardusz.scholax.shared.upload;

import io.github.bernardusz.scholax.shared.upload.dto.UploadCreation;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UploadRepository {
  private final JdbcClient jdbcClient;
  public UploadRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public Optional<Long> save(UploadCreation uploadCreation){
    return jdbcClient.sql(
      """
      INSERT INTO uploads
      (file_name, url, google_file_id)
      VALUES (:fileName, :url, :googleFileId)
      ON CONFLICT (google_file_id)
      DO UPDATE SET 
        file_name = EXCLUDED.file_name,
        url = EXCLUDED.url,
      RETURNING id
      """
    ).param("fileName", uploadCreation.fileName())
      .param("url", uploadCreation.url())
      .param("googleFileId", uploadCreation.googleFileId())
      .query(Long.class)
      .optional();
  }

  public Optional<String> findByUrlId(Long id){
    return jdbcClient.sql("SELECT url FROM uploads WHERE id = :id")
      .param("id", id)
      .query(String.class)
      .optional();
  }

  public boolean deleteById(Long id) {
    int updatedRows = jdbcClient.sql("DELETE FROM uploads WHERE id = :id")
      .param("id", id)
      .update();
    return updatedRows > 0;
  }
}
