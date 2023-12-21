package coffeemeet.server.certification.service;

import static coffeemeet.server.common.fixture.CertificationFixture.businessCardUrl;
import static coffeemeet.server.common.fixture.CertificationFixture.certificationPageable;
import static coffeemeet.server.common.fixture.CertificationFixture.companyName;
import static coffeemeet.server.common.fixture.CertificationFixture.department;
import static coffeemeet.server.common.fixture.CertificationFixture.email;
import static coffeemeet.server.common.fixture.CertificationFixture.pendingCertificationPage;
import static coffeemeet.server.common.fixture.CertificationFixture.verificationCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.implement.BusinessCardImageDeleter;
import coffeemeet.server.certification.implement.BusinessCardImageUploader;
import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.certification.implement.CompanyEmailValidator;
import coffeemeet.server.certification.implement.VerificationCodeGenerator;
import coffeemeet.server.certification.implement.VerificationCodeValidator;
import coffeemeet.server.certification.implement.VerificationInfoCommand;
import coffeemeet.server.certification.implement.VerificationInfoQuery;
import coffeemeet.server.certification.implement.VerificationMailSender;
import coffeemeet.server.certification.service.dto.PendingCertificationPageDto;
import java.io.File;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CertificationServiceTest {

  @InjectMocks
  private CertificationService certificationService;
  @Mock
  private BusinessCardImageUploader businessCardImageUploader;
  @Mock
  private BusinessCardImageDeleter businessCardImageDeleter;
  @Mock
  private CertificationCommand certificationCommand;
  @Mock
  private CertificationQuery certificationQuery;
  @Mock
  private CompanyEmailValidator companyEmailValidator;
  @Mock
  private VerificationCodeGenerator verificationCodeGenerator;
  @Mock
  private VerificationCodeValidator verificationCodeValidator;
  @Mock
  private VerificationInfoQuery verificationInfoQuery;
  @Mock
  private VerificationInfoCommand verificationInfoCommand;
  @Mock
  private VerificationMailSender verificationMailSender;

  @Test
  @DisplayName("인증 정보를 등록할 수 있다.")
  void registerCertificationTest() {
    // given
    Long userId = 1L;
    String companyName = companyName();
    String email = email();
    String departmentName = department().name();
    File businessCardImage = Instancio.create(File.class);
    String expectedImageUrl = businessCardUrl();

    given(businessCardImageUploader.uploadBusinessCardImage(any(File.class)))
        .willReturn(expectedImageUrl);

    // when
    certificationService.registerCertification(userId, companyName, email, departmentName,
        businessCardImage);

    // then
    then(certificationCommand).should(only()).createCertification(eq(userId), eq(companyName),
        any(CompanyEmail.class), any(Department.class), eq(expectedImageUrl));
  }

  @Test
  @DisplayName("인증 정보를 수정할 수 있다.")
  void updateCertificationTest() {
    // given
    long userId = 1L;
    String companyName = companyName();
    String email = email();
    String departmentName = department().name();
    File businessCardImage = Instancio.create(File.class);
    String expectedImageUrl = businessCardUrl();

    given(businessCardImageUploader.uploadBusinessCardImage(any(File.class))).willReturn(
        expectedImageUrl);

    // when
    certificationService.updateCertification(userId, companyName, email, departmentName,
        businessCardImage);

    //then
    then(businessCardImageDeleter).should(only()).deleteBusinessCardImageByUserId(userId);
    then(certificationCommand).should(only()).updateCertification(eq(userId), eq(companyName),
        any(CompanyEmail.class), any(Department.class), eq(expectedImageUrl));
  }

  @Test
  @DisplayName("회사 인증 메일을 전송할 수 있다.")
  void sendVerificationMailTest() {
    // given
    Long userId = 1L;
    String email = email();
    String verificationCode = verificationCode();

    given(verificationCodeGenerator.generateVerificationCode()).willReturn(verificationCode);

    // when
    certificationService.sendVerificationMail(userId, email);

    // then
    then(companyEmailValidator).should(only())
        .validateDuplicatedCompanyEmail(any(CompanyEmail.class));
    then(verificationMailSender).should(only())
        .sendVerificationMail(any(CompanyEmail.class), eq(verificationCode));
    then(verificationInfoCommand).should(only())
        .createVerificationInfo(eq(userId), any(CompanyEmail.class), eq(verificationCode));
  }

  @Test
  @DisplayName("인증 코드를 비교할 수 있다.")
  void compareCodeTest() {
    // given
    Long userId = 1L;
    String userInputCode = "123456";
    String actualVerificationCode = "123456";

    given(verificationInfoQuery.getVerificationCodeById(userId)).willReturn(actualVerificationCode);

    // when
    certificationService.compareCode(userId, userInputCode);

    // then
    then(verificationCodeValidator).should(only())
        .validateVerificationCode(eq(actualVerificationCode), eq(userInputCode));
  }

  @Test
  @DisplayName("미인증 사용자 요청을 페이지로 가져올 수 있다.")
  void getUncertifiedUserRequestsTest() {
    // given
    Pageable pageable = certificationPageable();
    Page<Certification> certificationPage = pendingCertificationPage(
        pageable.getPageSize());

    given(certificationQuery.getPendingCertification(any(Pageable.class))).willReturn(
        certificationPage);

    // when
    PendingCertificationPageDto result = certificationService.getUncertifiedUserRequests(pageable);

    // then
    assertThat(result).isNotNull();
    assertThat(result.page()).hasSize(certificationPage.getSize());
  }

}
