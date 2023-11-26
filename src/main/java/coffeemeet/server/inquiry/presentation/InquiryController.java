package coffeemeet.server.inquiry.presentation;

import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.domain.AuthInfo;
import coffeemeet.server.inquiry.presentation.dto.InquiryHTTP;
import coffeemeet.server.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
public class InquiryController {

  private final InquiryService inquiryService;

  @PostMapping
  public ResponseEntity<Void> inquire(@Login AuthInfo authInfo,
      @Valid @RequestBody InquiryHTTP.Request request) {
    inquiryService.registerInquiry(authInfo.userId(), request.title(), request.content());
    return ResponseEntity.ok().build();
  }

}
