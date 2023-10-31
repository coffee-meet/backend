package coffeemeet.server.user.service;

import static coffeemeet.server.certification.domain.Department.IT;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static coffeemeet.server.common.media.S3MediaService.KeyType.PROFILE_IMAGE;
import static coffeemeet.server.interest.domain.Keyword.COOK;
import static coffeemeet.server.interest.domain.Keyword.GAME;
import static coffeemeet.server.user.domain.OAuthProvider.KAKAO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.service.cq.CertificationQuery;
import coffeemeet.server.common.fixture.dto.AuthTokensFixture;
import coffeemeet.server.common.fixture.dto.OAuthUserInfoDtoFixture;
import coffeemeet.server.common.fixture.dto.SignupDtoFixture;
import coffeemeet.server.common.fixture.entity.CertificationFixture;
import coffeemeet.server.common.media.S3MediaService;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.service.cq.InterestCommand;
import coffeemeet.server.interest.service.cq.InterestQuery;
import coffeemeet.server.oauth.dto.OAuthUserInfoDto;
import coffeemeet.server.oauth.service.OAuthService;
import coffeemeet.server.user.domain.Birth;
import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.controller.dto.SignupHttpDto.Request;
import coffeemeet.server.user.service.dto.OAuthUserInfo;
import coffeemeet.server.user.service.dto.UserProfileDto.Response;
import coffeemeet.server.user.service.cq.UserCommand;
import coffeemeet.server.user.service.cq.UserQuery;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
  private S3MediaService s3MediaService;

  @Mock
  private OAuthService oAuthService;

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

  @DisplayName("회원가입을 할 수 있다.")
  @Test
  void signupTest() {
    // given
    Request request = SignupDtoFixture.signupDto();
    AuthTokens authTokens = AuthTokensFixture.authTokens();
    OAuthUserInfoDto.Response response = OAuthUserInfoDtoFixture.response();
    OAuthUserInfo userInfo = OAuthUserInfo.from(response);
    User user = user();

    given(oAuthService.getOAuthUserInfo(any(), any())).willReturn(userInfo);
    given(userCommand.saveUser(any(User.class))).willReturn(user.getId());
    given(userQuery.getUserById(user.getId())).willReturn(user);
    willDoNothing().given(interestCommand).saveAll(any(), any());
    given(authTokensGenerator.generate(user.getId())).willReturn(authTokens);

    // when
    AuthTokens result = userService.signup(request.nickname(), request.keywords(),
        request.authCode(), request.oAuthProvider());

    // then
    assertThat(result.accessToken()).isEqualTo(authTokens.accessToken());
    assertThat(result.refreshToken()).isEqualTo(authTokens.refreshToken());
  }

  @DisplayName("로그인을 할 수 있다.")
  @Test
  void loginTest() {
    // given
    User user = user();
    String authCode = "authCode";
    AuthTokens authTokens = AuthTokensFixture.authTokens();

    OAuthUserInfoDto.Response response = OAuthUserInfoDtoFixture.response();
    OAuthUserInfo userInfo = OAuthUserInfo.from(response);

    given(oAuthService.getOAuthUserInfo(any(), any())).willReturn(userInfo);
    given(userQuery.getUserByOAuthInfo(any(), any())).willReturn(user);
    given(authTokensGenerator.generate(anyLong())).willReturn(authTokens);

    // when
    AuthTokens result = userService.login(KAKAO, authCode);

    // then
    assertThat(result.accessToken()).isEqualTo(authTokens.accessToken());
    assertThat(result.refreshToken()).isEqualTo(authTokens.refreshToken());
  }

  @DisplayName("사용자의 프로필을 조회할 수 있다.")
  @Test
  void findUserProfileTest() {
    // given
    User user = user();
    Certification certification = CertificationFixture.certification();
    List<Keyword> keywords = new ArrayList<>(Arrays.asList(COOK, GAME));
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
        () -> assertThat(result.profileImageUrl()).isEqualTo(response.profileImageUrl()),
        () -> assertThat(result.interests()).isEqualTo(response.interests())
    );
  }

  @DisplayName("본인의 프로필을 조회할 수 있다.")
  @Test
  void findMyProfileTest() {
    // given
    User user = user();
    List<Keyword> keywords = new ArrayList<>(List.of(COOK));
    MyProfileDto.Response response = MyProfileDto.Response.of(user, keywords, IT);
    Certification certification = Certification.builder()
        .department(response.department())
        .user(user)
        .companyEmail(new CompanyEmail("company123@gmail.com"))
        .businessCardUrl("businessCard")
        .build();

    given(userQuery.getUserById(anyLong())).willReturn(user);
    given(interestQuery.getKeywordsByUserId(anyLong())).willReturn(response.interests());
    given(certificationQuery.getCertificationByUserId(anyLong())).willReturn(certification);

    // when
    MyProfileDto.Response result = userService.findMyProfile(user.getId());

    // then
    assertAll(
        () -> assertThat(result.name()).isEqualTo(response.name()),
        () -> assertThat(result.nickname()).isEqualTo(response.nickname()),
        () -> assertThat(result.email()).isEqualTo(response.email()),
        () -> assertThat(result.profileImageUrl()).isEqualTo(response.profileImageUrl()),
        () -> assertThat(result.birthYear()).isEqualTo(response.birthYear()),
        () -> assertThat(result.birthDay()).isEqualTo(response.birthDay()),
        () -> assertThat(result.department()).isEqualTo(response.department()),
        () -> assertThat(result.interests()).isEqualTo(response.interests())
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
    given(s3MediaService.generateKey(PROFILE_IMAGE)).willReturn("key");
    given(s3MediaService.getUrl(anyString())).willReturn("newImageUrl");

    // when
    userService.updateProfileImage(userId, file);

    // then
    assertThat(user.getProfile().getProfileImageUrl()).isEqualTo("newImageUrl");
  }

  @DisplayName("프로필 정보를 수정할 수 있다.")
  @Transactional
  @Test
  void updateProfileInfo() {
    // given
    User user = new User(new OAuthInfo(KAKAO, "123"), Profile.builder()
        .nickname("닉네임")
        .name("이름")
        .birth(new Birth("2001", "1018"))
        .profileImageUrl("http://imageUrl")
        .email(new Email("test123@gmail.com"))
        .build());

    String newNickname = "새닉네임";
    ArrayList<Keyword> newKeywords = new ArrayList<>(Arrays.asList(COOK, GAME));

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

    // when
    userService.deleteUser(user.getId());

    // then
    assertThat(true).isTrue();
  }

  @DisplayName("닉네임 중복을 검사할 수 있다.")
  @Test
  void checkDuplicatedNickname() {
    // given
    User user = user();
    String nickname = user.getProfile().getNickname();

    willDoNothing().given(userQuery).hasDuplicatedNickname(nickname);

    // when
    userService.checkDuplicatedNickname(nickname);

    // then
    assertThat(true).isTrue();
  }

}
