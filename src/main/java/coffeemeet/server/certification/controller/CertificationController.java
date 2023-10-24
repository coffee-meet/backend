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
@RequestMapping("/certification")
public class CertificationController {

  private final CertificationService certificationService;

  @PostMapping("/users/business-card")
  public ResponseEntity<Void> uploadBusinessCard(
      @Login
      AuthInfo authInfo,
      @NotNull
      @RequestPart("businessCard")
      MultipartFile businessCard
  ) {
    certificationService.uploadBusinessCard(authInfo.userId(),
        FileUtils.convertMultipartFileToFile(businessCard));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/users/company-mail")
  public ResponseEntity<Void> sendVerificationCodeByEmail(
      @Login
      AuthInfo authInfo,
      @Valid @RequestBody
      EmailDto emailDto
  ) {
    certificationService.sendVerificationMail(authInfo.userId(), emailDto.companyEmail());
    return ResponseEntity.ok().build();

  }

  @PostMapping("/users/company-mail/verification")
  public ResponseEntity<Void> verifyEmail(
      @Login
      AuthInfo authInfo,
      @Valid @RequestBody
      VerificationCodeDto verificationCodeDto
  ) {
    certificationService.verifyEmail(authInfo.userId(), verificationCodeDto.verificationCode());
    return ResponseEntity.ok().build();
  }

}
