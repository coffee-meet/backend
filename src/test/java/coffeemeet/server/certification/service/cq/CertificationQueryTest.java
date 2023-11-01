package coffeemeet.server.certification.service.cq;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.repository.CertificationRepository;
import coffeemeet.server.user.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificationQueryTest {

  @InjectMocks
  private CertificationQuery certificationQuery;
  @Mock
  private CertificationRepository certificationRepository;

  @Test
  @DisplayName("유저 아이디로 인증 정보를 가져올 수 있다.")
  void getCertificationByUserIdTest() {
    // given
    User user = user();
    Long userId = user.getId();
    Certification certification = certification(user);

    given(certificationRepository.findByUserId(userId)).willReturn(Optional.of(certification));

    // when
    Certification foundCertification = certificationQuery.getCertificationByUserId(userId);

    // then
    assertThat(foundCertification).isEqualTo(certification);
  }

}
