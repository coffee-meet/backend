package coffeemeet.server.certification.service;

import static coffeemeet.server.certification.exception.CertificationErrorCode.INVALID_VERIFICATION_CODE;
import static coffeemeet.server.common.media.S3MediaService.KeyType.BUSINESS_CARD;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.service.cq.CertificationCommand;
import coffeemeet.server.certification.service.cq.EmailVerificationCommand;
import coffeemeet.server.certification.service.cq.EmailVerificationQuery;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.common.media.EmailService;
import coffeemeet.server.common.media.S3MediaService;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.service.cq.UserQuery;
import java.io.File;
import java.util.random.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {

  private static final String WRONG_VERIFICATION_CODE_MESSAGE = "사용자(%s)가 잘못된 인증코드(%s)를 입력했습니다.";
  private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.getDefault();

  private final S3MediaService s3MediaService;
  private final EmailService emailService;
  private final UserQuery userQuery;
  private final CertificationCommand certificationCommand;
  private final EmailVerificationCommand emailVerificationCommand;
  private final EmailVerificationQuery emailVerificationQuery;

  public void registerCertification(long userId, String email, String departmentName,
      File businessCardImage) {
    String key = s3MediaService.generateKey(BUSINESS_CARD);
    uploadBusinessCard(userId, key, businessCardImage);

    CompanyEmail companyEmail = new CompanyEmail(email);
    String businessCardUrl = s3MediaService.getUrl(key);
    Department department = Department.valueOf(departmentName);
    User user = userQuery.getUserById(userId);
    certificationCommand.newCertification(companyEmail, businessCardUrl, department, user);
  }

  private void uploadBusinessCard(long userId, String key, File businessCardUrl) {
    certificationCommand.applyIfCertifiedUser(userId, certification -> {
      String oldKey = s3MediaService.extractKey(certification.getBusinessCardUrl(), BUSINESS_CARD);
      s3MediaService.delete(oldKey);
    });

    s3MediaService.upload(key, businessCardUrl);
    FileUtils.delete(businessCardUrl);
  }

  public void sendVerificationMail(Long userId, String email) {
    CompanyEmail companyEmail = new CompanyEmail(email);
    certificationCommand.hasDuplicatedCompanyEmail(companyEmail);

    String verificationCode = generateVerificationCode();
    emailService.sendVerificationCode(companyEmail, verificationCode);
    emailVerificationCommand.newEmailVerification(userId, companyEmail, verificationCode);
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
