package coffeemeet.server.certification.controller;

import static org.springframework.http.HttpStatus.CREATED;

import coffeemeet.server.certification.service.CertificationService;
import coffeemeet.server.common.util.FileUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping("/users/{userId}/business-card")
  @ResponseStatus(CREATED)
  public void uploadBusinessCard(
      @PathVariable("userId")
      @NotNull(message = "유저 ID는 null일 수 없습니다.")
      long userId,
      @RequestPart("businessCard")
      @NotNull(message = "명함 이미지는 null일 수 없습니다.")
      MultipartFile businessCard
  ) {
    certificationService.uploadBusinessCard(userId,
        FileUtils.convertMultipartFileToFile(businessCard));
  }

}
