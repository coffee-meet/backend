package coffeemeet.server.certification.presentation;

import coffeemeet.server.certification.presentation.dto.EmailHTTP;
import coffeemeet.server.certification.presentation.dto.VerificationCodeHTTP;
import coffeemeet.server.certification.service.CertificationService;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.domain.AuthInfo;
import coffeemeet.server.common.util.FileUtils;
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
      @RequestPart("companyName") @NotNull String companyName,
      @RequestPart("companyEmail") @NotNull String companyEmail,
      @RequestPart("department") @NotNull String department,
      @RequestPart("businessCard") @NotNull MultipartFile businessCardImage
  ) {
    certificationService.registerCertification(authInfo.userId(), companyName, companyEmail, department,
        FileUtils.convertMultipartFileToFile(businessCardImage));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/users/me/company-mail")
  public ResponseEntity<Void> sendVerificationCodeByEmail(
      @Login AuthInfo authInfo,
      @Valid @RequestBody EmailHTTP.Request request
  ) {
    certificationService.sendVerificationMail(authInfo.userId(), request.companyEmail());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/users/me/company-mail/verification")
  public ResponseEntity<Void> verifyEmail(
      @Login AuthInfo authInfo,
      @Valid @RequestBody VerificationCodeHTTP.Request request
  ) {
    certificationService.compareCode(authInfo.userId(), request.verificationCode());
    return ResponseEntity.ok().build();
  }

}
