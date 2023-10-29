package coffeemeet.server.certification.service.cq;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.businessCardUrl;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.companyEmail;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.department;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.repository.CertificationRepository;
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
  Consumer<Certification> consumer;
  @InjectMocks
  private CertificationCommand certificationCommand;
  @Mock
  private CertificationRepository certificationRepository;

  @Test
  @DisplayName("새로운 Certification 객체를 저장할 수 있다.")
  void newCertificationTest() {
    // given
    CompanyEmail companyEmail = companyEmail();
    String businessCardUrl = businessCardUrl();
    Department department = department();
    User user = user();

    // when
    certificationCommand.newCertification(companyEmail, businessCardUrl, department, user);

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
            IllegalArgumentException.class)
        .hasMessage("이미 사용 중인 회사 이메일입니다."); // todo 에러메세지 public으로 두는게 어떨까요?
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

}
