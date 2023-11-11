package coffeemeet.server.matching.presentation;

import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.domain.AuthInfo;
import coffeemeet.server.matching.presentation.dto.AcceptResponseHttp;
import coffeemeet.server.matching.service.MatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matching")
public class MatchingController {

  private final MatchingService matchingService;

  @PostMapping("/start")
  public ResponseEntity<Void> start(@Login AuthInfo authInfo) {
    matchingService.start(authInfo.userId());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/decision")
  public ResponseEntity<Void> acceptDecision(@Valid @RequestBody AcceptResponseHttp.Request request) {
    matchingService.processMatchDecision(
        request.userId(),
        request.isAccepted(),
        request.waitingRoomId()
    );
    return ResponseEntity.ok().build();
  }

}
