package coffeemeet.server.certification.service;

import coffeemeet.server.certification.domain.VerificationCode;
import coffeemeet.server.certification.repository.VerificationCodeRepository;
import coffeemeet.server.common.media.EmailService;
import coffeemeet.server.common.media.S3MediaService;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.domain.CompanyEmail;
import coffeemeet.server.user.service.UserService;
import java.io.File;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {

  private final S3MediaService s3MediaService;
  private final UserService userService;
  private final EmailService emailService;
  private final VerificationCodeRepository verificationCodeRepository;

  public void uploadBusinessCard(long userId, File file) {
    String key = s3MediaService.generateBusinessCardKey();
    s3MediaService.upload(key, file);
    userService.updateBusinessCardUrl(userId, s3MediaService.getUrl(key));

    FileUtils.delete(file);
  }

  public void verifyMail(Long userId, String companyEmail) {
    String verificationCode = emailService.generateVerificationCode();
    emailService.sendVerificationMail(new CompanyEmail(companyEmail), verificationCode);
    verificationCodeRepository.save(
        new VerificationCode(userId, verificationCode, LocalDateTime.now()));
  }

}
