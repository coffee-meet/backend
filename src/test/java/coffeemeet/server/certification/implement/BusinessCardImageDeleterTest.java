package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.domain.S3KeyPrefix.BUSINESS_CARD;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.common.implement.ImageDeleter;
import coffeemeet.server.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BusinessCardImageDeleterTest {

  @InjectMocks
  private BusinessCardImageDeleter businessCardImageDeleter;
  @Mock
  private CertificationQuery certificationQuery;
  @Mock
  private ImageDeleter imageDeleter;

  @Test
  void deleteBusinessCardImageByUserIdTest() {
    // given
    User user = user();
    Certification certification = certification(user);
    Long userId = user.getId();

    given(certificationQuery.getCertificationByUserId(userId)).willReturn(certification);

    // when
    businessCardImageDeleter.deleteBusinessCardImageByUserId(userId);

    // then
    then(imageDeleter).should(only())
        .deleteImage(certification.getBusinessCardUrl(), BUSINESS_CARD);
  }
}
