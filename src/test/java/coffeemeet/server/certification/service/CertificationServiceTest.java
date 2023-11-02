package coffeemeet.server.certification.service;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.businessCardUrl;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.department;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.email;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.verificationCode;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.certification.implement.EmailVerificationCommand;
import coffeemeet.server.certification.implement.EmailVerificationQuery;
import coffeemeet.server.common.implement.EmailSender;
import coffeemeet.server.common.implement.MediaManager;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.io.File;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

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
  private EmailVerificationCommand emailVerificationCommand;

  @Mock
  private EmailVerificationQuery emailVerificationQuery;

  @Test
  @DisplayName("회사 정보를 등록할 수 있다.")
  void registerCertificationTest() {
    // given
    User user = user();
    Long userId = user.getId();
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
    certificationService.registerCertification(userId, email, departmentName, file);

    // then
    then(mediaManager).should().generateKey(any());
    then(mediaManager).should().upload(any(), any(File.class));
    then(certificationCommand).should().createCertification(any(), any(), any(), any());

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

}
