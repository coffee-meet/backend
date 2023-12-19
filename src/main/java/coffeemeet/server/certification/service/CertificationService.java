package coffeemeet.server.certification.service;

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
import coffeemeet.server.certification.implement.EmailVerificationCommand;
import coffeemeet.server.certification.implement.EmailVerificationQuery;
import coffeemeet.server.certification.service.dto.PendingCertification;
import coffeemeet.server.certification.service.dto.PendingCertificationPageDto;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {

  private final BusinessCardImageUploader businessCardImageUploader;
  private final BusinessCardImageDeleter businessCardImageDeleter;
  private final CertificationCommand certificationCommand;
  private final CertificationQuery certificationQuery;
  private final CompanyEmailValidator companyEmailValidator;
  private final VerificationCodeGenerator verificationCodeGenerator;
  private final VerificationCodeValidator verificationCodeValidator;
  private final VerificationInfoQuery verificationInfoQuery;
  private final VerificationInfoCommand verificationInfoCommand;
  private final VerificationMailSender verificationMailSender;

  public void registerCertification(Long userId, String companyName, String email,
      String departmentName, File businessCardImage) {
    CompanyEmail companyEmail = new CompanyEmail(email);
    Department department = Department.valueOf(departmentName);

    String businessCardImageUrl = businessCardImageUploader.uploadBusinessCardImage(
        businessCardImage);

    certificationCommand.createCertification(userId, companyName, companyEmail, department,
        businessCardImageUrl);
  }

  public void updateCertification(Long userId, String companyName, String email,
      String departmentName, File businessCardImage) {
    CompanyEmail companyEmail = new CompanyEmail(email);
    Department department = Department.valueOf(departmentName);

    businessCardImageDeleter.deleteBusinessCardImageByUserId(userId);
    String businessCardImageUrl = businessCardImageUploader.uploadBusinessCardImage(
        businessCardImage);

    certificationCommand.updateCertification(userId, companyName, companyEmail, department,
        businessCardImageUrl);
  }

  public void sendVerificationMail(Long userId, String email) {
    CompanyEmail companyEmail = new CompanyEmail(email);
    companyEmailValidator.validateDuplicatedCompanyEmail(companyEmail);

    String verificationCode = verificationCodeGenerator.generateVerificationCode();
    verificationMailSender.sendVerificationMail(companyEmail, verificationCode);

    verificationInfoCommand.createVerificationInfo(userId, companyEmail, verificationCode);
  }

  public void compareCode(Long userId, String userInputCode) {
    String verificationCode = verificationInfoQuery.getVerificationCodeById(userId);
    verificationCodeValidator.validateVerificationCode(verificationCode, userInputCode);
  }

  public PendingCertificationPageDto getUncertifiedUserRequests(Pageable pageable) {
    Page<Certification> pendingCertifications = certificationQuery.getPendingCertification(
        pageable);
    return PendingCertificationPageDto.from(pendingCertifications);
  }

}
