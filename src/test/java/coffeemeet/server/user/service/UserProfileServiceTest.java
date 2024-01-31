package coffeemeet.server.user.service;

import static coffeemeet.server.common.domain.S3KeyPrefix.PROFILE_IMAGE;
import static coffeemeet.server.common.fixture.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.UserFixture.keywords;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.common.infrastructure.S3ObjectStorage;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.InterestCommand;
import coffeemeet.server.user.implement.InterestQuery;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.UserProfileDto;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

  @InjectMocks
  private UserProfileService userProfileService;

  @Mock
  private S3ObjectStorage objectStorage;

  @Mock
  private UserQuery userQuery;

  @Mock
  private InterestQuery interestQuery;

  @Mock
  private CertificationQuery certificationQuery;

  @Mock
  private UserCommand userCommand;

  @Mock
  private InterestCommand interestCommand;

  @DisplayName("사용자의 프로필을 조회할 수 있다.")
  @Test
  void findUserProfileTest() {
    // given
    User user = user();
    Certification certification = certification();
    List<Keyword> keywords = keywords();
    UserProfileDto response = UserProfileDto.of(user, keywords, certification);

    given(userQuery.getUserById(anyLong())).willReturn(user);
    given(interestQuery.getKeywordsByUserId(anyLong())).willReturn(keywords);
    given(certificationQuery.getCertificationByUserId(anyLong())).willReturn(certification);

    // when
    UserProfileDto result = userProfileService.findUserProfile(user.getId());

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
    Certification certification = certification(user);
    List<Keyword> keywords = keywords();
    MyProfileDto response = MyProfileDto.of(user, keywords, certification);

    given(userQuery.getUserById(anyLong())).willReturn(user);
    given(interestQuery.getKeywordsByUserId(anyLong())).willReturn(response.interests());
    given(certificationQuery.getCertificationByUserId(anyLong())).willReturn(certification);

    // when
    MyProfileDto result = userProfileService.findMyProfile(user.getId());

    // then
    assertAll(
        () -> assertThat(result.nickname()).isEqualTo(response.nickname()),
        () -> assertThat(result.profileImageUrl()).isEqualTo(response.profileImageUrl()),
        () -> assertThat(result.companyName()).isEqualTo(response.companyName()),
        () -> assertThat(result.department()).isEqualTo(response.department()),
        () -> assertThat(result.department()).isEqualTo(response.department())
    );
  }


  @DisplayName("프로필 사진을 수정할 수 있다.")
  @Test
  void updateProfileImage() throws IOException {
    // given
    User user = user();
    File file = File.createTempFile("temp", "png");

    given(userQuery.getUserById(anyLong())).willReturn(user);
    given(objectStorage.generateKey(PROFILE_IMAGE)).willReturn("key");
    given(objectStorage.getUrl(anyString())).willReturn("newImageUrl");
    given(objectStorage.extractKey(any(), eq(PROFILE_IMAGE))).willReturn("");

    // when
    userProfileService.updateProfileImage(user.getId(), file);

    // then
    assertThat(user.getOauthInfo().getProfileImageUrl()).isEqualTo("newImageUrl");
  }

  @DisplayName("프로필 정보를 수정할 수 있다.")
  @Transactional
  @Test
  void updateProfileInfo() {
    // given
    User user = user();

    String newNickname = "새닉네임";
    List<Keyword> newKeywords = keywords();

    given(userQuery.getUserById(any())).willReturn(user);
    willDoNothing().given(userCommand).updateUserInfo(any(), any());
    willDoNothing().given(interestCommand).updateInterests(any(), any());

    // when
    userProfileService.updateProfileInfo(user.getId(), newNickname, newKeywords);

    // then
    verify(userCommand).updateUserInfo(any(User.class), anyString());
    verify(interestCommand).updateInterests(any(User.class), anyList());
  }

}