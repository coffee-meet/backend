package coffeemeet.server.common.domain;

import java.io.File;

public interface ObjectStorage {

  void upload(String key, File file);

  void delete(String key);

  String getUrl(String key);

  String generateKey(S3KeyPrefix s3KeyPrefix);

  String extractKey(String url, S3KeyPrefix s3KeyPrefix);

}
