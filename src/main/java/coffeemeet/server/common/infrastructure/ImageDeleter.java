package coffeemeet.server.common.implement;

import coffeemeet.server.common.domain.S3KeyPrefix;
import coffeemeet.server.common.domain.ObjectStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageDeleter {

  private final ObjectStorage objectStorage;

  public void deleteImage(String imageUrl, S3KeyPrefix s3KeyPrefix) {
    String key = objectStorage.extractKey(imageUrl, s3KeyPrefix);
    objectStorage.delete(key);
  }

}
