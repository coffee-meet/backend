package coffeemeet.server.certification.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import coffeemeet.server.certification.dto.EmailDto;
import coffeemeet.server.certification.service.CertificationService;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.dto.AuthInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/certification")
public class CertificationController {

  private final CertificationService certificationService;

  @PostMapping("/users/business-card")
  @ResponseStatus(CREATED)
  public void uploadBusinessCard(
      @Login
      AuthInfo authInfo,
      @RequestPart("businessCard")
      @NotNull
      MultipartFile businessCard
  ) {
    certificationService.uploadBusinessCard(authInfo.userId(),
        FileUtils.convertMultipartFileToFile(businessCard));
  }

  @PostMapping("users/company-mail")
  @ResponseStatus(OK)
  public void sendVerificationMail(
      @Login
      AuthInfo authInfo,
      @RequestBody @Valid
      EmailDto emailDto
  ) {
    certificationService.sendVerificationMail(authInfo.userId(), emailDto.companyEmail());
  }

}
