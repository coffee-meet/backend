package coffeemeet.server.certification.service;

import static coffeemeet.server.common.fixture.CertificationFixture.businessCardUrl;
import static coffeemeet.server.common.fixture.CertificationFixture.certifications;
import static coffeemeet.server.common.fixture.CertificationFixture.companyName;
import static coffeemeet.server.common.fixture.CertificationFixture.department;
import static coffeemeet.server.common.fixture.CertificationFixture.email;
import static coffeemeet.server.common.fixture.CertificationFixture.verificationCode;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.certification.implement.EmailVerificationCommand;
import coffeemeet.server.certification.implement.EmailVerificationQuery;
import coffeemeet.server.certification.service.dto.PendingCertificationPageDto;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.common.implement.EmailSender;
import coffeemeet.server.common.implement.MediaManager;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.io.File;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CertificationServiceTest {

  @InjectMocks
  private CertificationService certificationService;

  @Mock
  private MediaManager mediaManager;

  @Mock
  private EmailSender emailSender;

  @Mock
  private UserQuery userQuery;

  @Mock
  private CertificationCommand certificationCommand;

  @Mock
  private CertificationQuery certificationQuery;

  @Mock
  private EmailVerificationCommand emailVerificationCommand;

  @Mock
  private EmailVerificationQuery emailVerificationQuery;

  @Test
  @DisplayName("회사 정보를 등록할 수 있다.")
  void registerCertificationTest() {
    // given
    User user = user();
    Long userId = user.getId();
    String companyName = companyName();
    String email = email();
    String departmentName = department().name();
    File file = mock();
    String businessCardUrl = businessCardUrl();

    MockedStatic<FileUtils> fileUtils = mockStatic(FileUtils.class);
    fileUtils.when(() -> FileUtils.delete(file)).then(invocation -> null);

    given(mediaManager.generateKey(any())).willReturn("someKey");
    given(mediaManager.getUrl(any())).willReturn(businessCardUrl);
    given(userQuery.getUserById(userId)).willReturn(user);

    // when
    certificationService.registerCertification(userId, companyName, email, departmentName, file);

    // then
    then(mediaManager).should().generateKey(any());
    then(mediaManager).should().upload(any(), any());
    then(certificationCommand).should().createCertification(any(), any(), any(), any(), any());

    fileUtils.close();
  }

  @Test
  @DisplayName("회사 정보를 수정할 수 있다.")
  void updateCertificationTest() {
    // given
    User user = user();
    Long userId = user.getId();
    String companyName = companyName();
    String email = email();
    String departmentName = department().name();
    File file = mock();
    String businessCardUrl = businessCardUrl();

    MockedStatic<FileUtils> fileUtils = mockStatic(FileUtils.class);
    fileUtils.when(() -> FileUtils.delete(file)).then(invocation -> null);

    given(mediaManager.generateKey(any())).willReturn("someKey");
    given(mediaManager.getUrl(any())).willReturn(businessCardUrl);
    given(userQuery.getUserById(userId)).willReturn(user);

    // when
    certificationService.updateCertification(userId, companyName, email, departmentName, file);

    // then

    then(mediaManager).should().upload(any(), any());
    then(certificationCommand).should().deleteCertification(any());
    then(certificationCommand).should()
        .createCertification(any(), any(), any(), any(), any());

    fileUtils.close();
  }

  @Test
  @DisplayName("회사 인증 메일을 전송할 수 있다.")
  void sendVerificationMailTest() {
    // given
    String email = email();
    User user = user();
    Long userId = user.getId();

    // when
    certificationService.sendVerificationMail(userId, email);

    // then
    then(certificationCommand).should(only()).hasDuplicatedCompanyEmail(any());
    then(emailSender).should(only()).sendVerificationCode(any(), anyString());
    then(emailVerificationCommand).should(only())
        .createEmailVerification(anyLong(), any(), anyString());
  }

  @Test
  @DisplayName("인증 코드를 비교할 수 있다.")
  void compareCodeTest() {
    // given
    Long userId = Instancio.create(Long.class);
    String verificationCode = verificationCode();
    given(emailVerificationQuery.getCodeById(userId)).willReturn(verificationCode);

    // when, then
    assertThatCode(() -> certificationService.compareCode(userId, verificationCode))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("인증 코드가 일치하지 않다면 예외를 던진다.")
  void compareCodeFailTest() {
    // given
    Long userId = Instancio.create(Long.class);
    String correctCode = "correctCode";
    String verificationCode = verificationCode();
    given(emailVerificationQuery.getCodeById(userId)).willReturn(correctCode);

    // when, then
    assertThatThrownBy(() -> certificationService.compareCode(userId, verificationCode))
        .isInstanceOf(InvalidInputException.class);
  }

  @Test
  @DisplayName("미인증 사용자 요청을 페이지로 가져올 수 있다.")
  void getUncertifiedUserRequestsTest() {
    // given
    Pageable pageable = mock(Pageable.class);
    List<Certification> certificationList = certifications();
    Page<Certification> pendingCertificationPage = new PageImpl<>(certificationList);

    given(certificationQuery.getPendingCertification(pageable)).willReturn(
        pendingCertificationPage);

    // when
    PendingCertificationPageDto result = certificationService.getUncertifiedUserRequests(pageable);

    // then
    assertThat(result).isNotNull();
    assertThat(result.page().getContent()).hasSize(certificationList.size());
  }

}
