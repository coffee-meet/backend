package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.fixture.CertificationFixture.businessCardUrl;
import static coffeemeet.server.common.fixture.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.CertificationFixture.companyEmail;
import static coffeemeet.server.common.fixture.CertificationFixture.companyName;
import static coffeemeet.server.common.fixture.CertificationFixture.department;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.infrastructure.CertificationRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.user.domain.User;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificationCommandTest {

  @Mock
  private Consumer<Certification> consumer;
  @InjectMocks
  private CertificationCommand certificationCommand;
  @Mock
  private CertificationRepository certificationRepository;
  @Mock
  private CertificationQuery certificationQuery;

  @Test
  @DisplayName("새로운 Certification 객체를 저장할 수 있다.")
  void createCertificationTest() {
    // given
    String companyName = companyName();
    CompanyEmail companyEmail = companyEmail();
    String businessCardUrl = businessCardUrl();
    Department department = department();
    User user = user();

    // when
    certificationCommand.createCertification(user, companyName, companyEmail, department,
        businessCardUrl);

    // then
    then(certificationRepository).should(only()).save(any(Certification.class));
  }

  @Test
  @DisplayName("중복된 회사 이메일이 있을 경우 예외가 발생한다.")
  void checkDuplicatedCompanyEmailTest() {
    // given
    CompanyEmail companyEmail = companyEmail();
    given(certificationRepository.existsByCompanyEmail(companyEmail)).willReturn(true);

    // when, then
    assertThatThrownBy(
        () -> certificationCommand.hasDuplicatedCompanyEmail(companyEmail)).isInstanceOf(
        InvalidInputException.class);
  }

  @Test
  @DisplayName("만약 인증이 완료된 사용자면 Consumer를 실행할 수 있다.")
  void applyIfCertifiedUserTest() {
    // given
    User user = user();
    Long userId = user.getId();
    Certification certification = certification(user());

    given(certificationRepository.findByUserId(userId)).willReturn(Optional.of(certification));

    // when
    certificationCommand.applyIfCertifiedUser(userId, consumer);

    // then
    then(consumer).should(only()).accept(certification);
  }

  @Test
  @DisplayName("사용자 인증을 완료시킬 수 있다.")
  void certificatedTest() {
    // given
    Certification certification = certification();
    Long userId = certification.getUser().getId();
    given(certificationQuery.getCertificationByUserId(userId)).willReturn(certification);

    // when
    certificationCommand.certificated(userId);

    // then
    assertThat(certification.isCertificated()).isTrue();
  }

  @Test
  @DisplayName("인증 정보를 삭제할 수 있다.")
  void deleteUserCertification() {
    // given
    Long userId = 1L;

    // when
    certificationCommand.deleteCertification(userId);

    // then
    then(certificationRepository).should(only()).deleteById(userId);
  }

}
