package coffeemeet.server.oauth.controller;

import coffeemeet.server.oauth.service.OAuthService;
import coffeemeet.server.user.domain.OAuthProvider;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2.0")
public class OAuthController {

  private final OAuthService oAuthService;

  @GetMapping("/{oAuthProvider}")
  public ResponseEntity<Void> redirectAuthCodeRequestUrl(@PathVariable OAuthProvider oAuthProvider,
      HttpServletResponse response) throws IOException {
    String redirectUrl = oAuthService.getAuthCodeRequestUrl(oAuthProvider);
    response.sendRedirect(redirectUrl);
    return new ResponseEntity<>(HttpStatus.FOUND);
  }

}
