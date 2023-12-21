package coffeemeet.server.common.fixture;

import static java.time.LocalDateTime.now;
import static org.instancio.Select.field;

import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.presentation.dto.LoginDetailsHTTP;
import coffeemeet.server.user.presentation.dto.LoginDetailsHTTP.Response;
import coffeemeet.server.user.presentation.dto.MyProfileHTTP;
import coffeemeet.server.user.presentation.dto.NotificationTokenHTTP;
import coffeemeet.server.user.presentation.dto.SignupHTTP;
import coffeemeet.server.user.presentation.dto.SignupHTTP.Request;
import coffeemeet.server.user.presentation.dto.UpdateProfileHTTP;
import coffeemeet.server.user.presentation.dto.UserProfileHTTP;
import coffeemeet.server.user.presentation.dto.UserStatusHTTP;
import coffeemeet.server.user.service.dto.LoginDetailsDto;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.UserProfileDto;
import coffeemeet.server.user.service.dto.UserStatusDto;
import java.util.List;
import java.util.Set;
import org.instancio.Instancio;

public class UserFixture {

  public static User user() {
    return Instancio.of(User.class).set(field(User::getProfile), profile())
        .set(field(User::isRegistered), true)
        .ignore(field(User::isDeleted))
        .ignore(field(User::isBlacklisted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  public static User user(Long userId) {
    return Instancio.of(User.class).set(field(User::getProfile), profile())
        .set(field(User::isRegistered), true)
        .set(field(User::getId), userId)
        .ignore(field(User::isDeleted))
        .ignore(field(User::isBlacklisted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  public static User user(UserStatus userStatus) {
    return Instancio.of(User.class).set(field(User::getProfile), profile())
        .set(field(User::getUserStatus), userStatus)
        .ignore(field(User::isDeleted))
        .ignore(field(User::isBlacklisted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  public static User userExcludingStatus(UserStatus excludingStatus) {
    return Instancio.of(User.class).set(field(User::getProfile), profile())
        .generate(field(User::getUserStatus),
            gen -> gen.enumOf(UserStatus.class).excluding(excludingStatus))
        .ignore(field(User::isDeleted))
        .ignore(field(User::isBlacklisted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  public static List<User> users() {
    return Instancio.ofList(User.class)
        .generate(field(User::getId), gen -> gen.longSeq().start(1L))
        .ignore(field(User::isDeleted))
        .ignore(field(User::isBlacklisted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  public static List<User> usersWithNullId() {
    return Instancio.ofList(User.class)
        .set(field(User::getId), null)
        .ignore(field(User::isDeleted))
        .ignore(field(User::isBlacklisted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  public static List<User> users(int size) {
    return Instancio.ofList(User.class)
        .size(size)
        .generate(field(User::getId), gen -> gen.longSeq().start(1L))
        .ignore(field(User::isDeleted))
        .ignore(field(User::isBlacklisted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  public static List<User> fourUsers() {
    return Instancio.ofList(User.class).size(4)
        .generate(field(User::getId), gen -> gen.longSeq().start(1L))
        .create();
  }

  private static Profile profile() {
    return Instancio.of(Profile.class)
        .generate(field(Profile::getNickname), gen -> gen.string().maxLength(20)).create();
  }

  public static NotificationTokenHTTP.Request notificationTokenHTTPRequest() {
    return Instancio.create(NotificationTokenHTTP.Request.class);
  }

  public static String token() {
    return Instancio.create(String.class);
  }

  public static NotificationInfo notificationInfo() {
    return Instancio.of(NotificationInfo.class)
        .set(field(NotificationInfo::isSubscribedToNotification), true)
        .generate(field(NotificationInfo::getCreatedTokenAt),
            gen -> gen.temporal().localDateTime().range(now().minusMonths(2), now())).create();
  }

  public static Set<NotificationInfo> notificationInfos() {
    return Instancio.ofSet(NotificationInfo.class)
        .set(field(NotificationInfo::isSubscribedToNotification), true)
        .generate(field(NotificationInfo::getCreatedTokenAt),
            gen -> gen.temporal().localDateTime().range(now().minusMonths(2), now())).create();
  }

  public static String content() {
    return Instancio.create(String.class);
  }

  public static Set<User> user(int size) {
    return Instancio.ofSet(User.class).size(size)
        .create();
  }

  public static User userWithFixedId(Long id) {
    return Instancio.of(User.class)
        .set(field(User::getProfile), profile())
        .set(field(User::getId), id)
        .ignore(field(User::isDeleted))
        .ignore(field(User::isBlacklisted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  public static LoginDetailsDto loginDetailsDto() {
    return Instancio.of(LoginDetailsDto.class)
        .create();
  }

  public static LoginDetailsHTTP.Response loginDetailsHTTPResponse(
      LoginDetailsDto response) {
    return new Response(
        response.userId(),
        response.isRegistered(),
        response.accessToken(),
        response.refreshToken(),
        response.nickname(),
        response.profileImageUrl(),
        response.companyName(),
        response.department(),
        response.interests()
    );
  }

  public static MyProfileDto myProfileDtoResponse() {
    return Instancio.of(MyProfileDto.class)
        .create();
  }

  public static MyProfileHTTP.Response myProfileHTTPResponse(MyProfileDto response) {
    return new MyProfileHTTP.Response(
        response.nickname(),
        response.profileImageUrl(),
        response.companyName(),
        response.department(),
        response.interests(),
        response.oAuthProvider()
    );
  }

  public static UserStatusDto userStatusDto() {
    return Instancio.of(UserStatusDto.class)
        .create();
  }

  public static SignupHTTP.Request signupHTTPRequest() {
    return Instancio.of(SignupHTTP.Request.class)
        .set(field(Request::keywords), keywords())
        .create();
  }

  public static List<Keyword> keywords() {
    return Instancio.ofList(Keyword.class)
        .size(3)
        .create();
  }

  public static UpdateProfileHTTP.Request updateProfileHTTPRequest() {
    return Instancio.of(UpdateProfileHTTP.Request.class)
        .set(field(UpdateProfileHTTP.Request::interests), keywords())
        .create();
  }

  public static UserProfileDto userProfileDtoResponse() {
    return Instancio.of(UserProfileDto.class)
        .create();
  }

  public static UserProfileHTTP.Response userProfileHTTPResponse(UserProfileDto response) {
    return new UserProfileHTTP.Response(
        response.nickname(),
        response.profileImageUrl(),
        response.department(),
        response.interests()
    );
  }

  public static UserStatusHTTP.Response userStatusHTTPResponse(UserStatusDto response) {
    return new UserStatusHTTP.Response(
        response.userStatus(),
        response.startedAt(),
        response.chattingRoomId(),
        response.chattingRoomName(),
        response.isCertificated(),
        response.penaltyExpiration()
    );
  }

}
