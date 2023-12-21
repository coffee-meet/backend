package coffeemeet.server.common.infrastructure;

import coffeemeet.server.common.domain.ObjectStorage;
import coffeemeet.server.common.domain.S3KeyPrefix;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class S3ObjectStorage implements ObjectStorage {

  private final AmazonS3 amazonS3;
  private final String bucketName;

  public S3ObjectStorage(
      AmazonS3 amazonS3,
      @Value("${cloud.aws.s3.bucket}") String bucketName
  ) {
    this.amazonS3 = amazonS3;
    this.bucketName = bucketName;
  }

  @Override
  public void upload(String key, File file) {
    try {
      amazonS3.putObject(bucketName, key, file);
    } catch (AmazonServiceException e) {
      log.warn("이미지 업로드에 실패 했습니다. key: " + key);
    }
  }

  @Override
  public void delete(String key) {
    try {
      amazonS3.deleteObject(bucketName, key);
    } catch (AmazonServiceException e) {
      log.warn("이미지 삭제에 실패 했습니다. key: " + key);
    }
  }

  @Override
  public String getUrl(String key) {
    return amazonS3.getUrl(bucketName, key).toExternalForm();
  }

  @Override
  public String generateKey(S3KeyPrefix s3KeyPrefix) {
    return String.format("%s-%s-%s", s3KeyPrefix.getValue(), LocalDateTime.now(),
        UUID.randomUUID());
  }

  @Override
  public String extractKey(String s3Url, S3KeyPrefix s3KeyPrefix) {
    int startIndex = s3Url.indexOf(s3KeyPrefix.getValue());
    if (startIndex == -1) {
      return "";
    }
    return s3Url.substring(startIndex);
  }

}
