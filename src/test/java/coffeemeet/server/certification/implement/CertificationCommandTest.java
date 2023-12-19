package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.businessCardUrl;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.companyEmail;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.companyName;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.department;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.pendingCertification;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static coffeemeet.server.common.fixture.CertificationFixture.businessCardUrl;
import static coffeemeet.server.common.fixture.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.CertificationFixture.companyEmail;
import static coffeemeet.server.common.fixture.CertificationFixture.companyName;
import static coffeemeet.server.common.fixture.CertificationFixture.department;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.domain.repository.CertificationRepository;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificationCommandTest {

  @InjectMocks
  private CertificationCommand certificationCommand;
  @Mock
  private CertificationRepository certificationRepository;
  @Mock
  private CertificationQuery certificationQuery;
  @Mock
  private UserQuery userQuery;

  @Test
  @DisplayName("새로운 Certification 객체를 저장할 수 있다.")
  void createCertificationTest() {
    // given
    User user = user();
    String companyName = companyName();
    CompanyEmail companyEmail = companyEmail();
    String businessCardUrl = businessCardUrl();
    Department department = department();
    Long userId = user.getId();

    given(userQuery.getUserById(userId)).willReturn(user);

    // when
    certificationCommand.createCertification(userId, companyName, companyEmail, department,
        businessCardUrl);

    // then
    then(certificationRepository).should(only()).save(any(Certification.class));
  }

  @Test
  @DisplayName("사용자 인증을 완료시킬 수 있다.")
  void certificatedTest() {
    // given
    Certification certification = certification();
    Long userId = certification.getUser().getId();

    given(certificationQuery.getCertificationByUserId(userId)).willReturn(certification);

    // when
    certificationCommand.completeCertification(userId);

    // then
    assertThat(certification.isCertificated()).isTrue();
  }

  @Test
  @DisplayName("인증 정보를 삭제할 수 있다.")
  void deleteUserCertification() {
    // given
    Long userId = 1L;

    // when
    certificationCommand.deleteCertificationByUserId(userId);

    // then
    then(certificationRepository).should(only()).deleteByUserId(userId);
  }

  @Test
  @DisplayName("인증 완료 시킬 수 있다.")
  void completeCertificationTest() {
    // given
    User user = user();
    Long userId = user.getId();
    Certification certification = pendingCertification(user);

    given(certificationQuery.getCertificationByUserId(userId)).willReturn(certification);

    // when
    certificationCommand.completeCertification(certification.getId());

    // then
    assertThat(certification.isCertificated()).isTrue();
  }

}
