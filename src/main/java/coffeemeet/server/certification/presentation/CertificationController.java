package coffeemeet.server.certification.presentation;

import coffeemeet.server.certification.presentation.dto.EmailHTTP;
import coffeemeet.server.certification.presentation.dto.VerificationCodeHTTP;
import coffeemeet.server.certification.service.CertificationService;
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
      @RequestPart("userId") @NotNull String userId,
      @RequestPart("companyName") @NotNull String companyName,
      @RequestPart("companyEmail") @NotNull String companyEmail,
      @RequestPart("department") @NotNull String department,
      @RequestPart("businessCard") @NotNull MultipartFile businessCardImage
  ) {
    certificationService.registerCertification(Long.valueOf(userId), companyName, companyEmail,
        department,
        FileUtils.convertMultipartFileToFile(businessCardImage));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/users/me/company-mail")
  public ResponseEntity<Void> sendVerificationCodeByEmail(
      @Valid @RequestBody EmailHTTP.Request request
  ) {
    certificationService.sendVerificationMail(request.userId(), request.companyEmail());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/users/me/company-mail/verification")
  public ResponseEntity<Void> verifyEmail(
      @Valid @RequestBody VerificationCodeHTTP.Request request
  ) {
    certificationService.compareCode(request.userId(), request.verificationCode());
    return ResponseEntity.ok().build();
  }

}
