package coffeemeet.server.common.implement;

import static coffeemeet.server.common.execption.GlobalErrorCode.INVALID_S3_URL;

import coffeemeet.server.common.domain.KeyType;
import coffeemeet.server.common.execption.InvalidInputException;
import com.amazonaws.services.s3.AmazonS3;
import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3MediaManager implements MediaManager {

  private static final String INVALID_S3_URL_MESSAGE = "올바르지 않은 S3 URL(%s)입니다.";
  private final AmazonS3 amazonS3;
  private final String bucketName;

  public S3MediaManager(
      AmazonS3 amazonS3,
      @Value("${cloud.aws.s3.bucket}") String bucketName
  ) {
    this.amazonS3 = amazonS3;
    this.bucketName = bucketName;
  }

  @Override
  public void upload(String key, File file) {
    amazonS3.putObject(bucketName, key, file);
  }

  @Override
  public void delete(String key) {
    amazonS3.deleteObject(bucketName, key);
  }

  @Override
  public String getUrl(String key) {
    return amazonS3.getUrl(bucketName, key).toExternalForm();
  }

  @Override
  public String generateKey(KeyType keyType) {
    return String.format("%s-%s-%s", keyType.getValue(), LocalDateTime.now(),
        UUID.randomUUID());
  }

  @Override
  public String extractKey(String s3Url, KeyType keyType) {
    int startIndex = s3Url.indexOf(keyType.getValue());
    if (startIndex == -1) {
      throw new InvalidInputException(INVALID_S3_URL,
          String.format(INVALID_S3_URL_MESSAGE, s3Url));
    }
    return s3Url.substring(startIndex);
  }

}
