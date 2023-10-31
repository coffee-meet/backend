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

import coffeemeet.server.certification.service.cq.CertificationCommand;
import coffeemeet.server.certification.service.cq.EmailVerificationCommand;
import coffeemeet.server.certification.service.cq.EmailVerificationQuery;
import coffeemeet.server.common.media.EmailService;
import coffeemeet.server.common.media.S3MediaService;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.service.cq.UserQuery;
import java.io.File;
import org.instancio.Instancio;
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
  private S3MediaService s3MediaService;
  @Mock
  private EmailService emailService;
  @Mock
  private UserQuery userQuery;
  @Mock
  private CertificationCommand certificationCommand;
  @Mock
  private EmailVerificationCommand emailVerificationCommand;
  @Mock
  private EmailVerificationQuery emailVerificationQuery;

  @Test
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
    given(s3MediaService.generateKey(any())).willReturn("someKey");
    given(s3MediaService.getUrl(any())).willReturn(businessCardUrl);
    given(userQuery.getUserById(userId)).willReturn(user);

    // when
    certificationService.registerCertification(userId, email, departmentName, file);

    // then
    then(s3MediaService).should().generateKey(any());
    then(s3MediaService).should().upload(any(), any(File.class));
    then(certificationCommand).should().newCertification(any(), any(), any(), any());

    fileUtils.close();
  }

  @Test
  void sendVerificationMailTest() {
    // given
    String email = email();
    User user = user();
    Long userId = user.getId();

    // when
    certificationService.sendVerificationMail(userId, email);

    // then
    then(certificationCommand).should(only()).hasDuplicatedCompanyEmail(any());
    then(emailService).should(only()).sendVerificationCode(any(), anyString());
    then(emailVerificationCommand).should(only())
        .newEmailVerification(anyLong(), any(), anyString());
  }

  @Test
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
