package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.fixture.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.infrastructure.CertificationRepository;
import coffeemeet.server.user.domain.User;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

  @Test
  @DisplayName("유저 아이디로 회사 이름을 가져올 수 있다.")
  void getCompanyNameByUserIdTest() {
    // given
    User user = user();
    Long userId = user.getId();
    Certification certification = certification(user);

    given(certificationRepository.findByUserId(userId)).willReturn(Optional.of(certification));

    // when
    String companyName = certificationQuery.getCompanyNameByUserId(userId);

    // then
    assertThat(companyName).isEqualTo(certification.getCompanyName());
  }

  @Test
  @DisplayName("펜딩 중인 인증 정보를 페이지로 가져올 수 있다.")
  void getPendingCertificationTest() {
    // given
    Pageable pageable = Pageable.unpaged();
    Page<Certification> expectedPage = new PageImpl<>(Collections.emptyList());

    given(certificationRepository.findPendingCertifications(pageable)).willReturn(expectedPage);

    // when
    Page<Certification> certificationPage = certificationQuery.getPendingCertification(pageable);

    // then
    assertThat(certificationPage).isEqualTo(expectedPage);
  }

}
