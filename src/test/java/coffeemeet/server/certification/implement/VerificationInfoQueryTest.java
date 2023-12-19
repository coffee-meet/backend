package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.emailVerification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.certification.domain.VerificationInfo;
import coffeemeet.server.certification.domain.repository.VerificationInfoRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerificationInfoQueryTest {

  @InjectMocks
  private VerificationInfoQuery verificationInfoQuery;
  @Mock
  private VerificationInfoRepository verificationInfoRepository;

  @Test
  @DisplayName("유저 아이디로 VerificationInfo를 조회할 수 있다.")
  void getCodeByIdTest() {
    // given
    VerificationInfo verificationInfo = emailVerification();

    given(verificationInfoRepository.findById(verificationInfo.getUserId())).willReturn(
        Optional.of(verificationInfo));

    // when
    String code = verificationInfoQuery.getVerificationCodeById(verificationInfo.getUserId());

    // then
    assertThat(code).isEqualTo(verificationInfo.getVerificationCode());
  }

}
