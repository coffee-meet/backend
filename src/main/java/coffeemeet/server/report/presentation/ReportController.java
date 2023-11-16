package coffeemeet.server.report.presentation;

import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.domain.AuthInfo;
import coffeemeet.server.report.presentation.dto.ReportHTTP;
import coffeemeet.server.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

  private final ReportService reportService;

  @PostMapping
  public ResponseEntity<Void> reportUser(@Login AuthInfo authInfo,
      @Valid @RequestBody ReportHTTP.Request request) {
    reportService.reportUser(authInfo.userId(), request.chattingRoomId(), request.targetId(),
        request.reason(), request.reasonDetail());
    return ResponseEntity.ok().build();
  }

}
