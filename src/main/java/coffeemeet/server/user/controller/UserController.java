package coffeemeet.server.user.controller;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.controller.dto.MyProfileHttpDto;
import coffeemeet.server.user.controller.dto.UserProfileHttpDto;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.controller.dto.AuthInfo;
import coffeemeet.server.user.dto.MyProfileDto;
import coffeemeet.server.user.controller.dto.SignupHttpDto;
import coffeemeet.server.user.controller.dto.UpdateProfileHttpDto;
import coffeemeet.server.user.dto.UserProfileDto.Response;
import coffeemeet.server.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @PostMapping("/sign-up")
  public ResponseEntity<AuthTokens> signup(@Valid @RequestBody SignupHttpDto.Request request) {
    return ResponseEntity.ok(
        userService.signup(request.nickname(), request.keywords(), request.authCode(),
            request.oAuthProvider()));
  }

  @GetMapping("/login/{oAuthProvider}")
  public ResponseEntity<AuthTokens> login(@PathVariable OAuthProvider oAuthProvider,
      @RequestParam String authCode) {
    return ResponseEntity.ok(userService.login(oAuthProvider, authCode));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserProfileHttpDto.Response> findUserProfile(@PathVariable long userId) {
    Response response = userService.findUserProfile(userId);
    return ResponseEntity.ok(UserProfileHttpDto.Response.of(response));
  }

  @GetMapping("/me")
  public ResponseEntity<MyProfileHttpDto.Response> findMyProfile(@Login AuthInfo authInfo) {
    MyProfileDto.Response response = userService.findMyProfile(authInfo.userId());
    return ResponseEntity.ok(MyProfileHttpDto.Response.of(response));
  }

  @PostMapping("/me/profile-image")
  public ResponseEntity<Void> updateProfileImage(
      @Login AuthInfo authInfo,
      @RequestPart("profileImage")
      @NotNull MultipartFile profileImage) {
    userService.updateProfileImage(
        authInfo.userId(),
        FileUtils.convertMultipartFileToFile(profileImage));
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/me")
  public ResponseEntity<Void> updateProfileInfo(@Login AuthInfo authInfo,
      @Valid @RequestBody UpdateProfileHttpDto.Request request) {
    userService.updateProfileInfo(authInfo.userId(), request.nickname(), request.interests());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/duplicate")
  public ResponseEntity<Void> checkDuplicatedNickname(@RequestParam @NotBlank String nickname) {
    userService.checkDuplicatedNickname(nickname);
    return ResponseEntity.ok().build();
  }

}
