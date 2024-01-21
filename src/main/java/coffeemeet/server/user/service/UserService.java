package coffeemeet.server.user.service;

import static coffeemeet.server.common.execption.GlobalErrorCode.BAD_REQUEST_ERROR;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.common.execption.BadRequestException;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.oauth.domain.OAuthMemberDetail;
import coffeemeet.server.oauth.implement.client.OAuthMemberClientRegistry;
import coffeemeet.server.oauth.implement.client.OAuthMemberUnlinkRegistry;
import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.implement.InterestCommand;
import coffeemeet.server.user.implement.InterestQuery;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import coffeemeet.server.user.service.dto.LoginDetailsDto;
import coffeemeet.server.user.service.dto.UserStatusDto;
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

  private final OAuthMemberClientRegistry oAuthMemberClientRegistry;
  private final OAuthMemberUnlinkRegistry oAuthMemberUnlinkRegistry;

  private final CertificationQuery certificationQuery;
  private final AuthTokensGenerator authTokensGenerator;

  private final InterestQuery interestQuery;
  private final InterestCommand interestCommand;
  private final UserQuery userQuery;
  private final UserCommand userCommand;
  private final MatchingQueueCommand matchingQueueCommand;

  @Transactional
  public void signup(Long userId, String nickname, List<Keyword> keywords) {
    User user = userQuery.getNonRegisteredUserById(userId);
    userQuery.hasDuplicatedNickname(nickname);
    user.registerUser(new Profile(nickname));
    interestCommand.saveAll(keywords, user);
  }

  public LoginDetailsDto login(OAuthProvider oAuthProvider, String authCode) {
    OAuthMemberDetail memberDetail = oAuthMemberClientRegistry.fetch(oAuthProvider, authCode);
    OAuthInfo oauthInfo = new OAuthInfo(memberDetail.oAuthProvider(),
        memberDetail.oAuthProviderId(),
        new Email(memberDetail.email()), memberDetail.profileImage());
    User user = userQuery.getUserByOAuthInfoOrDefault(oauthInfo);

    if (user.isRegistered()) {
      // TODO: 12/21/23 회원가입 중간에 나갈 때 예외 터지는 오류 잡기
      if (user.isDeleted()) {
        if (user.getPrivacyDateTime().isBefore(LocalDateTime.now())) {
          user.deletedWithdraw();
          userCommand.updateUser(user);
          List<Keyword> interests = interestQuery.getKeywordsByUserId(user.getId());
          Certification certification = certificationQuery.getCertificationByUserId(user.getId());
          AuthTokens authTokens = authTokensGenerator.generate(user.getId());
          return LoginDetailsDto.of(user, interests, certification, authTokens);
        } else {
          userCommand.deleteUser(user.getId());
        }
      } else {
        List<Keyword> interests = interestQuery.getKeywordsByUserId(user.getId());
        Certification certification = certificationQuery.getCertificationByUserId(user.getId());
        AuthTokens authTokens = authTokensGenerator.generate(user.getId());
        return LoginDetailsDto.of(user, interests, certification, authTokens);
      }
    }
    userCommand.saveUser(user);
    return LoginDetailsDto.of(user, Collections.emptyList(), null, null);
  }

  public void checkDuplicatedNickname(String nickname) {
    userQuery.hasDuplicatedNickname(nickname);
  }

  public void deleteUser(Long userId, String accessToken, OAuthProvider oAuthProvider) {
    User user = userQuery.getUserById(userId);
    user.delete();
    oAuthMemberUnlinkRegistry.unlink(oAuthProvider, accessToken);
  }

  public void deleteUserInfos() {
    List<User> users = userQuery.getDeletedUsers();
    LocalDateTime today = LocalDateTime.now();

    List<User> deletedUsers = users.stream()
        .filter(u -> u.getPrivacyDateTime() != null)
        .filter(
            u -> u.getPrivacyDateTime().isAfter(today) || u.getPrivacyDateTime().isEqual(today))
        .toList();
    deletedUsers.forEach(u -> userCommand.deleteUser(u.getId()));
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
    return UserStatusDto.of(UserStatus.CHATTING_UNCONNECTED, null, chattingRoomId,
        chattingRoomName,
        null, null);
  }

  private UserStatusDto handleReportedUser(User user) {
    LocalDateTime penaltyExpiration = user.getReportInfo().getPenaltyExpiration();
    return UserStatusDto.of(UserStatus.REPORTED, null, null, null, null, penaltyExpiration);
  }

}
