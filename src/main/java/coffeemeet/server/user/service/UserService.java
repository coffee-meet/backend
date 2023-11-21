package coffeemeet.server.user.service;

import static coffeemeet.server.common.domain.KeyType.PROFILE_IMAGE;
import static coffeemeet.server.report.exception.ReportErrorCode.BLACKLIST_USER;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.common.execption.InvalidAuthException;
import coffeemeet.server.common.implement.MediaManager;
import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.oauth.implement.client.OAuthMemberClientComposite;
import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.InterestCommand;
import coffeemeet.server.user.implement.InterestQuery;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import coffeemeet.server.user.service.dto.LoginDetailsDto;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.UserProfileDto;
import java.io.File;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private static final String BLACKLIST_USER_MESSAGE = "해당 사용자(%s)는 영구정지된 사용자입니다.";

  private final MediaManager mediaManager;
  private final OAuthMemberClientComposite oAuthMemberClientComposite;

  private final CertificationQuery certificationQuery;
  private final AuthTokensGenerator authTokensGenerator;

  private final InterestQuery interestQuery;
  private final InterestCommand interestCommand;
  private final UserQuery userQuery;
  private final UserCommand userCommand;

  @Transactional
  public void signup(Long userId, String nickname, List<Keyword> keywords) {
    User user = userQuery.getNonRegisteredUserById(userId);
    userQuery.hasDuplicatedNickname(nickname);
    user.registerUser(new Profile(nickname));
    interestCommand.saveAll(keywords, user);
  }

  public LoginDetailsDto.Response login(OAuthProvider oAuthProvider, String authCode) {
    OAuthMemberDetail memberDetail = oAuthMemberClientComposite.fetch(oAuthProvider, authCode);
    OAuthInfo oauthInfo = new OAuthInfo(memberDetail.oAuthProvider(),
        memberDetail.oAuthProviderId(),
        new Email(memberDetail.email()), memberDetail.profileImage());
    if (userQuery.isRegistered(oauthInfo)) {
      User user = userQuery.getUserByOAuthInfo(oauthInfo);
      List<Keyword> interests = interestQuery.getKeywordsByUserId(user.getId());
      Certification certification = certificationQuery.getCertificationByUserId(user.getId());
      AuthTokens authTokens = authTokensGenerator.generate(user.getId());
      return LoginDetailsDto.Response.of(user, interests, certification, authTokens);
    }
    User user = new User(oauthInfo);
    userCommand.saveUser(user);
    return LoginDetailsDto.Response.of(user, Collections.emptyList(), null, null);
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
    deleteCurrentProfileImage(user.getOauthInfo().getProfileImageUrl());

    String key = mediaManager.generateKey(PROFILE_IMAGE);
    mediaManager.upload(key, file);
    user.updateProfileImageUrl(mediaManager.getUrl(key));
    userCommand.updateUser(user);
  }

  @Transactional
  public void updateProfileInfo(Long userId, String nickname,
      List<Keyword> keywords) {
    User user = userQuery.getUserById(userId);
    if (nickname != null) {
      userCommand.updateUserInfo(user, nickname);
    }
    if (keywords != null && !keywords.isEmpty()) {
      interestCommand.updateInterests(user, keywords);
    }
  }

  public void checkDuplicatedNickname(String nickname) {
    userQuery.hasDuplicatedNickname(nickname);
  }

  public void deleteUser(Long userId) {
    userCommand.deleteUser(userId);
  }

  public void registerOrUpdateNotificationToken(Long useId, String token) {
    userCommand.registerOrUpdateNotificationToken(useId, token);
  }

  public void unsubscribeNotification(Long userId) {
    userCommand.unsubscribeNotification(userId);
  }

  private void isBlacklist(User user) {
    if (user.isBlacklisted()) {
      throw new InvalidAuthException(
          BLACKLIST_USER,
          String.format(BLACKLIST_USER_MESSAGE, user.getProfile().getNickname())
      );
    }
  }

  private void deleteCurrentProfileImage(String profileImageUrl) {
    String currentKey = mediaManager.extractKey(profileImageUrl,
        PROFILE_IMAGE);
    mediaManager.delete(currentKey);
  }

}
