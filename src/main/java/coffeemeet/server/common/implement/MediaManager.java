package coffeemeet.server.common.implement;

import java.io.File;

public interface MediaManager {

  void upload(String key, File file);

  void delete(String key);

  String getUrl(String key);

  String generateKey(KeyType keyType);

  String extractKey(String s3Url, KeyType keyType);

}
