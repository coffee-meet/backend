package coffeemeet.server.user.service;

import static coffeemeet.server.common.media.S3MediaService.KeyType.PROFILE_IMAGE;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.service.cq.CertificationQuery;
import coffeemeet.server.common.media.S3MediaService;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.service.cq.InterestCommand;
import coffeemeet.server.interest.service.cq.InterestQuery;
import coffeemeet.server.oauth.service.OAuthService;
import coffeemeet.server.user.domain.Birth;
import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.dto.MyProfileDto;
import coffeemeet.server.user.dto.OAuthUserInfo;
import coffeemeet.server.user.dto.SignupDto;
import coffeemeet.server.user.dto.UserProfileDto;
import coffeemeet.server.user.service.cq.UserCommand;
import coffeemeet.server.user.service.cq.UserQuery;
import java.io.File;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private static final String DEFAULT_IMAGE_URL = "기본 이미지 URL";

  private final S3MediaService s3MediaService;
  private final OAuthService oAuthService;

  private final CertificationQuery certificationQuery;
  private final AuthTokensGenerator authTokensGenerator;

  private final InterestQuery interestQuery;
  private final InterestCommand interestCommand;
  private final UserQuery userQuery;
  private final UserCommand userCommand;

  public AuthTokens signup(SignupDto.Request request) {
    OAuthUserInfo oAuthUserInfo = oAuthService.getOAuthUserInfo(request.oAuthProvider(),
        request.authCode());

    userCommand.checkDuplicatedUser(oAuthUserInfo.oAuthProvider(), oAuthUserInfo.oAuthProviderId());
    userCommand.checkDuplicatedNickname(request.nickname());
    String profileImage = getProfileImageOrDefault(oAuthUserInfo.profileImage());

    User user = new User(new coffeemeet.server.user.domain.OAuthInfo(oAuthUserInfo.oAuthProvider(),
        oAuthUserInfo.oAuthProviderId()),
        Profile.builder().name(oAuthUserInfo.name()).nickname(request.nickname())
            .email(new Email(oAuthUserInfo.email()))
            .profileImageUrl(profileImage)
            .birth(new Birth(oAuthUserInfo.birthYear(), oAuthUserInfo.birthDay())).build());

    User newUser = userCommand.saveUser(user);
    interestCommand.saveInterests(request, newUser);
    return authTokensGenerator.generate(newUser.getId());
  }

  public AuthTokens login(OAuthProvider oAuthProvider, String authCode) {
    OAuthUserInfo oAuthUserInfo = oAuthService.getOAuthUserInfo(oAuthProvider, authCode);
    User user = userCommand.getUserByOAuthInfo(oAuthProvider, oAuthUserInfo.oAuthProviderId());
    return authTokensGenerator.generate(user.getId());
  }

  public UserProfileDto.Response findUserProfile(long userId) {
    User user = userQuery.getUserById(userId);
    List<Keyword> keywords = interestQuery.getKeywordsByUserId(userId);
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    return UserProfileDto.Response.of(user, certification.getDepartment(), keywords);
  }

  public MyProfileDto.Response findMyProfile(Long userId) {
    User user = userQuery.getUserById(userId);
    List<Keyword> keywords = interestQuery.getKeywordsByUserId(userId);
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    return MyProfileDto.Response.of(user, keywords, certification.getDepartment());
  }

  public void updateProfileImage(Long userId, File file) {
    User user = userQuery.getUserById(userId);
    deleteCurrentProfileImage(user.getProfile().getProfileImageUrl());
    String key = s3MediaService.generateKey(PROFILE_IMAGE);
    s3MediaService.upload(key, file);
    user.updateProfileImageUrl(s3MediaService.getUrl(key));
    userCommand.updateUser(user);
  }

  public void updateProfileInfo(Long userId, String nickname,
      List<Keyword> keywords) {
    User user = userQuery.getUserById(userId);
    userQuery.checkDuplicatedNickname(nickname);
    user.updateNickname(nickname);
    interestCommand.updateInterests(userQuery.getUserById(userId), keywords);
  }

  public void checkDuplicatedNickname(String nickname) {
    userCommand.checkDuplicatedNickname(nickname);
  }

  @Transactional
  public void deleteUser(Long userId) {
    userCommand.deleteUser(userId);
  }

  private String getProfileImageOrDefault(String profileImage) {
    if (profileImage == null) {
      profileImage = DEFAULT_IMAGE_URL;
    }
    return profileImage;
  }

  private void deleteCurrentProfileImage(String profileImageUrl) {
    String currentKey = s3MediaService.extractKey(profileImageUrl,
        PROFILE_IMAGE);
    s3MediaService.delete(currentKey);
  }

}
