package coffeemeet.server.user.presentation;

import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.annotation.PerformanceMeasurement;
import coffeemeet.server.common.domain.AuthInfo;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.presentation.dto.LoginDetailsHTTP;
import coffeemeet.server.user.presentation.dto.MyProfileHTTP;
import coffeemeet.server.user.presentation.dto.NotificationTokenHTTP;
import coffeemeet.server.user.presentation.dto.SignupHTTP;
import coffeemeet.server.user.presentation.dto.UpdateProfileHTTP;
import coffeemeet.server.user.presentation.dto.UserProfileHTTP;
import coffeemeet.server.user.presentation.dto.UserStatusHTTP;
import coffeemeet.server.user.service.UserService;
import coffeemeet.server.user.service.dto.LoginDetailsDto;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.UserProfileDto;
import coffeemeet.server.user.service.dto.UserStatusDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @PostMapping("/sign-up")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignupHTTP.Request request) {
    userService.signup(request.userId(), request.nickname(), request.keywords());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/login/{oAuthProvider}")
  public ResponseEntity<LoginDetailsHTTP.Response> login(@PathVariable OAuthProvider oAuthProvider,
      @RequestParam String authCode) {
    LoginDetailsDto response = userService.login(oAuthProvider, authCode);
    return ResponseEntity.ok(LoginDetailsHTTP.Response.of(response));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserProfileHTTP.Response> getUserProfile(@PathVariable Long userId) {
    UserProfileDto response = userService.findUserProfile(userId);
    return ResponseEntity.ok(UserProfileHTTP.Response.of(response));
  }

  @GetMapping("/me")
  public ResponseEntity<MyProfileHTTP.Response> getMyProfile(@Login AuthInfo authInfo) {
    MyProfileDto response = userService.findMyProfile(authInfo.userId());
    return ResponseEntity.ok(MyProfileHTTP.Response.of(response));
  }

  @PerformanceMeasurement
  @GetMapping("/status")
  public ResponseEntity<UserStatusHTTP.Response> getUserStatus(@Login AuthInfo authInfo) {
    log.info("유저 상태 요청 시간: " + LocalDateTime.now());
    UserStatusDto response = userService.getUserStatus(authInfo.userId());
    return ResponseEntity.ok(UserStatusHTTP.Response.of(response));
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
  public ResponseEntity<Void> updateProfileInfo( // @Login AuthInfo authInfo,
      @Valid @RequestBody UpdateProfileHTTP.Request request) {
    userService.updateProfileInfo(1L, request.nickname(), request.interests());
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
