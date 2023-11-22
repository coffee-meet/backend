package coffeemeet.server.user.service;

import static coffeemeet.server.common.domain.KeyType.PROFILE_IMAGE;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.common.implement.MediaManager;
import coffeemeet.server.oauth.implement.client.OAuthMemberClientComposite;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.InterestCommand;
import coffeemeet.server.user.implement.InterestQuery;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.UserProfileDto.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private MediaManager mediaManager;

  @Mock
  private OAuthMemberClientComposite oAuthMemberClientComposite;

  @Mock
  private AuthTokensGenerator authTokensGenerator;

  @Mock
  private InterestQuery interestQuery;

  @Mock
  private InterestCommand interestCommand;

  @Mock
  private UserQuery userQuery;

  @Mock
  private UserCommand userCommand;

  @Mock
  private CertificationQuery certificationQuery;

  // TODO: 11/21/23 회원가입 테스트 작성
//  @DisplayName("회원가입을 할 수 있다.")
//  @Test
//  void signupTest() {
//    // given
//    SignupHTTP.Request request = SignupHTTPFixture.signupHTTPRequest();
//    AuthTokens authTokens = AuthTokensFixture.authTokens();
//    OAuthMemberDetail response = OAuthUserInfoDtoFixture.response();
//    User user = user();
//
//    given(oAuthMemberClientComposite.fetch(any(), any())).willReturn(response);
//    given(userCommand.saveUser(any(User.class))).willReturn(user.getId());
//    given(userQuery.getUserById(user.getId())).willReturn(user);
//    willDoNothing().given(interestCommand).saveAll(any(), any());
//    given(authTokensGenerator.generate(user.getId())).willReturn(authTokens);
//
//    // when
//    userService.signup(user.getId(), request.nickname(), request.keywords());
//
//    // then
//
//  }

  // TODO: 11/21/23 로그인 테스트 작성
//  @DisplayName("로그인을 할 수 있다.")
//  @Test
//  void loginTest() {
//    // given
//    User user = user();
//    Certification certification = certification();
//    List<Keyword> keywords = UserFixture.keywords();
//    String authCode = "authCode";
//    AuthTokens authTokens = AuthTokensFixture.authTokens();
//
//    OAuthMemberDetail response = OAuthUserInfoDtoFixture.response();
//
//    given(oAuthMemberClientComposite.fetch(any(), any())).willReturn(response);
//    given(userQuery.getUserByOAuthInfo(any())).willReturn(user);
//    given(interestQuery.getKeywordsByUserId(anyLong())).willReturn(keywords);
//    given(certificationQuery.getCertificationByUserId(anyLong())).willReturn(certification);
//    given(authTokensGenerator.generate(anyLong())).willReturn(authTokens);
//
//    // when
//    LoginDetailsDto.Response result = userService.login(KAKAO, authCode);
//
//    // then
//    assertAll(
//        () -> assertThat(result.isRegistered()).isEqualTo(user.isRegistered()),
//        () -> assertThat(result.accessToken()).isEqualTo(authTokens.accessToken()),
//        () -> assertThat(result.refreshToken()).isEqualTo(authTokens.refreshToken()),
//        () -> assertThat(result.nickname()).isEqualTo(user.getProfile().getNickname()),
//        () -> assertThat(result.profileImageUrl()).isEqualTo(
//            user.getOauthInfo().getProfileImageUrl()),
//        () -> assertThat(result.companyName()).isEqualTo(certification.getCompanyName()),
//        () -> assertThat(result.department()).isEqualTo(certification.getDepartment())
//    );
//  }

  @DisplayName("사용자의 프로필을 조회할 수 있다.")
  @Test
  void findUserProfileTest() {
    // given
    User user = user();
    Certification certification = certification();
    List<Keyword> keywords = UserFixture.keywords();
    Response response = Response.of(user, certification.getDepartment(), keywords);

    given(userQuery.getUserById(anyLong())).willReturn(user);
    given(interestQuery.getKeywordsByUserId(user.getId())).willReturn(keywords);
    given(certificationQuery.getCertificationByUserId(anyLong())).willReturn(certification);

    // when
    Response result = userService.findUserProfile(user.getId());

    // then
    assertAll(
        () -> assertThat(result.nickname()).isEqualTo(response.nickname()),
        () -> assertThat(result.department()).isEqualTo(response.department()),
        () -> assertThat(result.profileImageUrl()).isEqualTo(response.profileImageUrl())
    );
  }

  @DisplayName("본인의 프로필을 조회할 수 있다.")
  @Test
  void findMyProfileTest() {
    // given
    User user = user();
    List<Keyword> keywords = UserFixture.keywords();
    Certification certification = certification(user);
    MyProfileDto.Response response = MyProfileDto.Response.of(user, keywords,
        certification.getCompanyName(),
        certification.getDepartment());

    given(userQuery.getUserById(anyLong())).willReturn(user);
    given(interestQuery.getKeywordsByUserId(anyLong())).willReturn(response.interests());
    given(certificationQuery.getCertificationByUserId(anyLong())).willReturn(certification);

    // when
    MyProfileDto.Response result = userService.findMyProfile(user.getId());

    // then
    assertAll(
        () -> assertThat(result.nickname()).isEqualTo(response.nickname()),
        () -> assertThat(result.profileImageUrl()).isEqualTo(response.profileImageUrl()),
        () -> assertThat(result.companyName()).isEqualTo(response.companyName()),
        () -> assertThat(result.department()).isEqualTo(response.department()),
        () -> assertThat(result.department()).isEqualTo(response.department())
    );
  }

  @DisplayName("아이디로 사용자를 조회할 수 있다.")
  @Test
  void getUserById() {
    // given
    User user = user();
    Long userId = user.getId();

    given(userQuery.getUserById(userId)).willReturn(user);

    // when
    User foundUser = userQuery.getUserById(userId);

    // then
    assertAll(
        () -> assertThat(foundUser.getId()).isEqualTo(user.getId()),
        () -> assertThat(foundUser.getOauthInfo().getOauthProvider()).isEqualTo(
            user.getOauthInfo().getOauthProvider()),
        () -> assertThat(foundUser.getOauthInfo().getOauthProviderId()).isEqualTo(
            user.getOauthInfo().getOauthProviderId()),
        () -> assertThat(foundUser.getProfile()).isEqualTo(user.getProfile())
    );
  }

  @DisplayName("프로필 사진을 수정할 수 있다.")
  @Test
  void updateProfileImage() throws IOException {
    // given
    User user = user();
    Long userId = user.getId();
    File file = File.createTempFile("temp", "png");

    given(userQuery.getUserById(userId)).willReturn(user);
    given(mediaManager.generateKey(PROFILE_IMAGE)).willReturn("key");
    given(mediaManager.getUrl(anyString())).willReturn("newImageUrl");

    // when
    userService.updateProfileImage(userId, file);

    // then
    assertThat(user.getOauthInfo().getProfileImageUrl()).isEqualTo("newImageUrl");
  }

  @DisplayName("프로필 정보를 수정할 수 있다.")
  @Transactional
  @Test
  void updateProfileInfo() {
    // given
    User user = UserFixture.user();

    String newNickname = "새닉네임";
    List<Keyword> newKeywords = UserFixture.keywords();

    given(userQuery.getUserById(any())).willReturn(user);
    willDoNothing().given(userCommand).updateUserInfo(any(), any());
    willDoNothing().given(interestCommand).updateInterests(any(), any());

    // when
    userService.updateProfileInfo(user.getId(), newNickname, newKeywords);

    // then
    verify(userCommand).updateUserInfo(any(User.class), anyString());
    verify(interestCommand).updateInterests(any(User.class), anyList());
  }

  @DisplayName("탈퇴할 수 있다.")
  @Test
  void deleteUser() {
    // given
    User user = user();

    willDoNothing().given(userCommand).deleteUser(user.getId());

    // when, then
    assertThatCode(() -> userService.deleteUser(user.getId()))
        .doesNotThrowAnyException();
  }

  @DisplayName("닉네임 중복을 검사할 수 있다.")
  @Test
  void checkDuplicatedNickname() {
    // given
    User user = user();
    String nickname = user.getProfile().getNickname();

    willDoNothing().given(userQuery).hasDuplicatedNickname(nickname);

    // then
    assertThatCode(() -> userService.checkDuplicatedNickname(nickname))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("푸시 알림 토큰을 등록 및 업데이트 할 수 있다.")
  void registerOrUpdateNotificationTokenTest() {
    // given
    willDoNothing().given(userCommand).registerOrUpdateNotificationToken(any(), any());

    // when
    userService.registerOrUpdateNotificationToken(any(), any());

    // then
    then(userCommand).should(only()).registerOrUpdateNotificationToken(any(), any());
  }

  @Test
  @DisplayName("푸시 알림을 거부할 수 있다.")
  void unsubscribeNotificationTest() {
    // given
    willDoNothing().given(userCommand).unsubscribeNotification(any());

    // when
    userService.unsubscribeNotification(any());

    // then
    then(userCommand).should(only()).unsubscribeNotification(any());
  }

}
