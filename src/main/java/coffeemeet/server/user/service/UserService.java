package coffeemeet.server.user.service;

import static coffeemeet.server.common.domain.S3KeyPrefix.PROFILE_IMAGE;
import static coffeemeet.server.common.execption.GlobalErrorCode.BAD_REQUEST_ERROR;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.DEFAULT_IMAGE_URL;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.common.domain.ObjectStorage;
import coffeemeet.server.common.execption.BadRequestException;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.oauth.implement.client.OAuthMemberClientComposite;
import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.implement.DuplicatedNicknameValidator;
import coffeemeet.server.user.implement.InterestCommand;
import coffeemeet.server.user.implement.InterestQuery;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import coffeemeet.server.user.service.dto.LoginDetailsDto;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.UserProfileDto;
import coffeemeet.server.user.service.dto.UserStatusDto;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private static final String INVALID_REQUEST_MESSAGE = "사용자 상태에 맞지 않는 요청입니다.";
  private final ObjectStorage objectStorage;
  private final OAuthMemberClientComposite oAuthMemberClientComposite;
  private final CertificationQuery certificationQuery;
  private final AuthTokensGenerator authTokensGenerator;
  private final InterestQuery interestQuery;
  private final InterestCommand interestCommand;
  private final UserQuery userQuery;
  private final UserCommand userCommand;
  private final MatchingQueueCommand matchingQueueCommand;
  private final DuplicatedNicknameValidator duplicatedNicknameValidator;

  @Transactional
  public void signup(Long userId, String nickname, List<Keyword> keywords) {
    User user = userQuery.getNonRegisteredUserById(userId);
    duplicatedNicknameValidator.validate(nickname);
    user.registerUser(new Profile(nickname));
    interestCommand.saveAll(keywords, user);
  }

  public LoginDetailsDto login(OAuthProvider oAuthProvider, String authCode) {
    OAuthMemberDetail memberDetail = oAuthMemberClientComposite.fetch(oAuthProvider, authCode);
    OAuthInfo oauthInfo = new OAuthInfo(memberDetail.oAuthProvider(),
        memberDetail.oAuthProviderId(),
        new Email(memberDetail.email()), memberDetail.profileImage());
    User user = userQuery.getUserByOAuthInfoOrDefault(oauthInfo);
    if (user.isRegistered()) {
      // TODO: 12/21/23 회원가입 중간에 나갈 때 예외 터지는 오류 잡기
      List<Keyword> interests = interestQuery.getKeywordsByUserId(user.getId());
      Certification certification = certificationQuery.getCertificationByUserId(user.getId());
      AuthTokens authTokens = authTokensGenerator.generate(user.getId());
      return LoginDetailsDto.of(user, interests, certification, authTokens);
    }
    userCommand.saveUser(user);
    return LoginDetailsDto.of(user, Collections.emptyList(), null, null);
  }

  public UserProfileDto findUserProfile(Long userId) {
    User user = userQuery.getUserById(userId);
    List<Keyword> keywords = interestQuery.getKeywordsByUserId(userId);
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    return UserProfileDto.of(user, keywords, certification);
  }

  public MyProfileDto findMyProfile(Long userId) {
    User user = userQuery.getUserById(userId);
    List<Keyword> keywords = interestQuery.getKeywordsByUserId(userId);
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    return MyProfileDto.of(user, keywords, certification);
  }

  public void updateProfileImage(Long userId, File file) {
    User user = userQuery.getUserById(userId);
    deleteCurrentProfileImage(user.getOauthInfo().getProfileImageUrl());

    String key = objectStorage.generateKey(PROFILE_IMAGE);
    objectStorage.upload(key, file);
    user.updateProfileImageUrl(objectStorage.getUrl(key));
    userCommand.updateUser(user);
  }

  @Transactional
  public void updateProfileInfo(Long userId, String nickname,
      List<Keyword> keywords) {
    userCommand.updateUserInfo(userId, nickname);
    interestCommand.updateInterests(userId, keywords);
  }

  public void checkDuplicatedNickname(String nickname) {
    duplicatedNicknameValidator.validate(nickname);
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

  public UserStatusDto getUserStatus(Long userId) {
    User user = userQuery.getUserById(userId);
    Certification certification = certificationQuery.getCertificationByUserId(userId);

    return switch (user.getUserStatus()) {
      case IDLE -> handleIdleUser(certification);
      case MATCHING -> handleMatchingUser(userId, certification);
      case CHATTING_UNCONNECTED -> handleChattingUser(user);
      case REPORTED -> handleReportedUser(user);
      case CHATTING_CONNECTED -> throw new BadRequestException(BAD_REQUEST_ERROR,
          INVALID_REQUEST_MESSAGE);
    };
  }

  private UserStatusDto handleIdleUser(Certification certification) {
    boolean isCertificated = certification != null;
    return UserStatusDto.of(UserStatus.IDLE, null, null, null, isCertificated, null);
  }

  private UserStatusDto handleMatchingUser(Long userId, Certification certification) {
    LocalDateTime startedAt = matchingQueueCommand.getTimeByUserId(certification.getCompanyName(),
        userId);
    return UserStatusDto.of(UserStatus.MATCHING, startedAt, null, null, null, null);
  }

  private UserStatusDto handleChattingUser(User user) {
    Long chattingRoomId = user.getChattingRoom().getId();
    String chattingRoomName = user.getChattingRoom().getName();
    return UserStatusDto.of(UserStatus.CHATTING_UNCONNECTED, null, chattingRoomId, chattingRoomName,
        null, null);
  }

  private UserStatusDto handleReportedUser(User user) {
    LocalDateTime penaltyExpiration = user.getReportInfo().getPenaltyExpiration();
    return UserStatusDto.of(UserStatus.REPORTED, null, null, null, null, penaltyExpiration);
  }

  private void deleteCurrentProfileImage(String profileImageUrl) {
    if (!profileImageUrl.equals(DEFAULT_IMAGE_URL)) {
      String currentKey = objectStorage.extractKey(profileImageUrl,
          PROFILE_IMAGE);
      if (currentKey.isBlank()) {
        return;
      }
      objectStorage.delete(currentKey);
    }
  }

}
