package coffeemeet.server.common.implement;

import coffeemeet.server.common.domain.ObjectStorage;
import coffeemeet.server.common.domain.S3KeyPrefix;
import coffeemeet.server.common.util.FileUtils;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageUploader {

  private final ObjectStorage objectStorage;

  public String uploadImage(File image, S3KeyPrefix s3KeyPrefix) {
    String key = objectStorage.generateKey(s3KeyPrefix);
    objectStorage.upload(key, image);
    FileUtils.deleteTempFile(image);
    return objectStorage.getUrl(key);
  }

}
