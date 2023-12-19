package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.domain.S3KeyPrefix.BUSINESS_CARD;

import coffeemeet.server.common.implement.ImageUploader;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessCardImageUploader {

  private final ImageUploader imageUploader;

  public String uploadBusinessCardImage(File businessCardImage) {
    return imageUploader.uploadImage(businessCardImage, BUSINESS_CARD);
  }

}
