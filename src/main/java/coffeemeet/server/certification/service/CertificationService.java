package coffeemeet.server.certification.service;

import static coffeemeet.server.certification.exception.CertificationErrorCode.INVALID_VERIFICATION_CODE;
import static coffeemeet.server.common.domain.KeyType.BUSINESS_CARD;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.implement.CertificationCommand;
import coffeemeet.server.certification.implement.EmailVerificationCommand;
import coffeemeet.server.certification.implement.EmailVerificationQuery;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.common.implement.EmailSender;
import coffeemeet.server.common.implement.MediaManager;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.io.File;
import java.util.random.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CertificationService {

  private static final String WRONG_VERIFICATION_CODE_MESSAGE = "사용자(%s)가 잘못된 인증코드(%s)를 입력했습니다.";
  private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.getDefault();

  private final MediaManager mediaManager;
  private final EmailSender emailSender;
  private final UserQuery userQuery;
  private final CertificationCommand certificationCommand;
  private final EmailVerificationCommand emailVerificationCommand;
  private final EmailVerificationQuery emailVerificationQuery;

  @Transactional
  public void registerCertification(long userId, String companyName, String email,
      String departmentName, File businessCardImage) {
    String key = mediaManager.generateKey(BUSINESS_CARD);
    uploadBusinessCard(userId, key, businessCardImage);

    CompanyEmail companyEmail = new CompanyEmail(email);
    String businessCardUrl = mediaManager.getUrl(key);
    Department department = Department.valueOf(departmentName);
    User user = userQuery.getUserById(userId);
    certificationCommand.createCertification(user, companyName, companyEmail, department,
        businessCardUrl);
  }

  private void uploadBusinessCard(long userId, String key, File businessCardUrl) {
    certificationCommand.applyIfCertifiedUser(userId, certification -> {
      String oldKey = mediaManager.extractKey(certification.getBusinessCardUrl(), BUSINESS_CARD);
      mediaManager.delete(oldKey);
    });

    mediaManager.upload(key, businessCardUrl);
    FileUtils.delete(businessCardUrl);
  }

  public void sendVerificationMail(Long userId, String email) {
    CompanyEmail companyEmail = new CompanyEmail(email);
    certificationCommand.hasDuplicatedCompanyEmail(companyEmail);

    String verificationCode = generateVerificationCode();
    emailSender.sendVerificationCode(companyEmail, verificationCode);
    emailVerificationCommand.createEmailVerification(userId, companyEmail, verificationCode);
  }

  public void compareCode(Long userId, String verificationCode) {
    String correctCode = emailVerificationQuery.getCodeById(userId);
    if (!correctCode.equals(verificationCode)) {
      throw new InvalidInputException(INVALID_VERIFICATION_CODE,
          String.format(WRONG_VERIFICATION_CODE_MESSAGE, userId, verificationCode));
    }
  }

  private String generateVerificationCode() {
    return String.format("%06d", RANDOM_GENERATOR.nextInt(1000000));
  }

}
