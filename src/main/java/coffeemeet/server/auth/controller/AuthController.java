package coffeemeet.server.auth.controller;

import coffeemeet.server.auth.dto.SignupRequest;
import coffeemeet.server.auth.service.AuthService;
import coffeemeet.server.auth.utils.AuthTokens;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.dto.AuthInfo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/auth")
public class AuthController {

  private final AuthService authService;

  @GetMapping("/{oAuthProvider}")
  public ResponseEntity<Void> redirectAuthCodeRequestUrl(@PathVariable OAuthProvider oAuthProvider,
      HttpServletResponse response) throws IOException {
    String redirectUrl = authService.getAuthCodeRequestUrl(oAuthProvider);
    response.sendRedirect(redirectUrl);
    return new ResponseEntity<>(HttpStatus.FOUND);
  }

  @PostMapping("/sign-up")
  public ResponseEntity<AuthTokens> signup(@Valid @RequestBody SignupRequest request) {
    return ResponseEntity.ok(authService.signup(request));
  }

  @GetMapping("/login/{oAuthProvider}")
  public ResponseEntity<AuthTokens> login(@PathVariable OAuthProvider oAuthProvider,
      @RequestParam String authCode) {
    return ResponseEntity.ok(authService.login(oAuthProvider, authCode));
  }

  @PostMapping("/renew-token")
  public ResponseEntity<AuthTokens> renew(@Login AuthInfo authInfo) {
    return ResponseEntity.ok(authService.renew(authInfo.userId(), authInfo.refreshToken()));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@Login AuthInfo authInfo) {
    authService.logout(authInfo.userId());
    return ResponseEntity.ok().build();
  }

}
