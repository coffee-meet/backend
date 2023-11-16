package coffeemeet.server.user.presentation;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.domain.AuthInfo;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.presentation.dto.LoginDetailsHTTP;
import coffeemeet.server.user.presentation.dto.MyProfileHTTP;
import coffeemeet.server.user.presentation.dto.NotificationTokenHTTP;
import coffeemeet.server.user.presentation.dto.SignupHTTP;
import coffeemeet.server.user.presentation.dto.UpdateProfileHTTP;
import coffeemeet.server.user.presentation.dto.UserProfileHTTP;
import coffeemeet.server.user.service.UserService;
import coffeemeet.server.user.service.dto.LoginDetailsDto;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.UserProfileDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity<AuthTokens> signup(@Valid @RequestBody SignupHTTP.Request request) {
    return ResponseEntity.ok(
        userService.signup(request.nickname(), request.keywords(), request.authCode(),
            request.oAuthProvider()));
  }

  @GetMapping("/login/{oAuthProvider}")
  public ResponseEntity<LoginDetailsHTTP.Response> login(@PathVariable OAuthProvider oAuthProvider,
      @RequestParam String authCode) {
    LoginDetailsDto.Response response = userService.login(oAuthProvider, authCode);
    return ResponseEntity.ok(LoginDetailsHTTP.Response.of(response));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserProfileHTTP.Response> findUserProfile(@PathVariable long userId) {
    UserProfileDto.Response response = userService.findUserProfile(userId);
    return ResponseEntity.ok(UserProfileHTTP.Response.of(response));
  }

  @GetMapping("/me")
  public ResponseEntity<MyProfileHTTP.Response> findMyProfile(@Login AuthInfo authInfo) {
    MyProfileDto.Response response = userService.findMyProfile(authInfo.userId());
    return ResponseEntity.ok(MyProfileHTTP.Response.of(response));
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
      @Valid @RequestBody UpdateProfileHTTP.Request request) {
    userService.updateProfileInfo(authInfo.userId(), request.nickname(), request.interests());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/duplicate")
  public ResponseEntity<Void> checkDuplicatedNickname(@RequestParam @NotBlank String nickname) {
    userService.checkDuplicatedNickname(nickname);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/notification/token")
  public ResponseEntity<Void> registerOrUpdateNotificationToken(
      @Login AuthInfo authInfo,
      @RequestBody NotificationTokenHTTP.Request request
  ) {
    userService.registerOrUpdateNotificationToken(authInfo.userId(), request.token());
    return ResponseEntity.ok().build();
  }

  @PutMapping("/notification/unsubscription")
  public ResponseEntity<Void> unsubscribeNotification(
      @Login AuthInfo authInfo
  ) {
    userService.unsubscribeNotification(authInfo.userId());
    return ResponseEntity.ok().build();
  }

}
