package coffeemeet.server.auth.presentation;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.service.AuthService;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.domain.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/renew-token")
  public ResponseEntity<AuthTokens> renew(@Login AuthInfo authInfo) {
    return ResponseEntity.ok(authService.renew(authInfo.userId(), authInfo.refreshToken()));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@Login AuthInfo authInfo) {
    authService.logout(authInfo.userId());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/delete")
  public ResponseEntity<Void> delete(@Login AuthInfo authInfo) {
    authService.delete(authInfo.userId(), authInfo.refreshToken());
    return ResponseEntity.ok().build();
  }

}
