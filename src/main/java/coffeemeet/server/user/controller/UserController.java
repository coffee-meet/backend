package coffeemeet.server.user.controller;

import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.user.dto.AuthInfo;
import coffeemeet.server.user.dto.MyProfileResponse;
import coffeemeet.server.user.dto.UserProfileResponse;
import coffeemeet.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @GetMapping("/{nickname}")
  public ResponseEntity<UserProfileResponse> findUserProfile(@PathVariable String nickname) {
    return ResponseEntity.ok(userService.findUserProfile(nickname));
  }

  @GetMapping("/me")
  public ResponseEntity<MyProfileResponse> findMyProfile(@Login AuthInfo authInfo) {
    return ResponseEntity.ok(userService.findMyProfile(authInfo.userId()));
  }

}
