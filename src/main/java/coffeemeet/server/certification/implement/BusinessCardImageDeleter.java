package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.domain.S3KeyPrefix.BUSINESS_CARD;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.common.implement.ImageDeleter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessCardImageDeleter {

  private final CertificationQuery certificationQuery;
  private final ImageDeleter imageDeleter;

  public void deleteBusinessCardImageByUserId(Long userId) {
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    imageDeleter.deleteImage(certification.getBusinessCardUrl(), BUSINESS_CARD);
  }

}
