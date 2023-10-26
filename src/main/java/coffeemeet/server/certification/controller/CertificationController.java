package coffeemeet.server.certification.controller;

import coffeemeet.server.certification.dto.EmailDto;
import coffeemeet.server.certification.dto.VerificationCodeDto;
import coffeemeet.server.certification.service.CertificationService;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.dto.AuthInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/certification")
public class CertificationController {

  private final CertificationService certificationService;

  @PostMapping("/users/me/company-info")
  public ResponseEntity<Void> registerCompanyInfo(
      @Login AuthInfo authInfo,
      @RequestPart("companyEmail") @NotNull String companyEmail,
      @RequestPart("department") @NotNull String department,
      @RequestPart("businessCard") @NotNull MultipartFile businessCardImage
  ) {
    certificationService.registerCertification(authInfo.userId(), companyEmail, department,
        FileUtils.convertMultipartFileToFile(businessCardImage));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/users/me/company-mail")
  public ResponseEntity<Void> sendVerificationCodeByEmail(
      @Login AuthInfo authInfo,
      @Valid @RequestBody EmailDto.Request request
  ) {
    certificationService.sendVerificationMail(authInfo.userId(), request.companyEmail());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/users/me/company-mail/verification")
  public ResponseEntity<Void> verifyEmail(
      @Login AuthInfo authInfo,
      @Valid @RequestBody VerificationCodeDto.Request request
  ) {
    certificationService.compareCode(authInfo.userId(), request.verificationCode());
    return ResponseEntity.ok().build();
  }

}
