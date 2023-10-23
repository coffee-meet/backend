package coffeemeet.server.certification.service;

import coffeemeet.server.common.media.S3MediaService;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.service.UserService;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {

  private final S3MediaService s3MediaService;
  private final UserService userService;
  private final BusinessCardS3KeyGenerator businessCardS3KeyGenerator;

  public void uploadBusinessCard(long userId, File file) {
    String key = businessCardS3KeyGenerator.generate();

    s3MediaService.upload(key, file);
    userService.updateBusinessCardUrl(userId, s3MediaService.getUrl(key));

    FileUtils.delete(file);
  }
}
