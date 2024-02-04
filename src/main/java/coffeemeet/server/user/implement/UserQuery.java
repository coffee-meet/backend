package coffeemeet.server.user.implement;

import static coffeemeet.server.user.exception.UserErrorCode.ALREADY_REGISTERED_USER;
import static coffeemeet.server.user.exception.UserErrorCode.NOT_EXIST_USER;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQuery {

  private static final String NOT_EXIST_USER_MESSAGE = "해당 아이디(%s)에 일치하는 사용자는 존재하지 않습니다.";
  private static final String ALREADY_REGISTERED_USER_MESSAGE = "해당 사용자(%s)는 이미 회원가입 되었습니다.";

  private final UserRepository userRepository;

  public User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(
            NOT_EXIST_USER,
            String.format(NOT_EXIST_USER_MESSAGE, userId))
        );
  }

  public User getNonRegisteredUserById(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(
            NOT_EXIST_USER,
            String.format(NOT_EXIST_USER_MESSAGE, userId))
        );
    if (user.isRegistered()) {
      throw new NotFoundException(
          ALREADY_REGISTERED_USER,
          String.format(ALREADY_REGISTERED_USER_MESSAGE, userId)
      );
    }
    return user;
  }

  public Set<User> getUsersByIdSet(Set<Long> userIds) {
    return new HashSet<>(userRepository.findByIdIn(userIds));
  }

  public Set<NotificationInfo> getNotificationInfosByIdSet(Set<Long> userIds) {
    return userRepository.findByIdIn(userIds).stream()
        .map(User::getNotificationInfo).collect(Collectors.toSet());
  }

  public User getUserByOAuthInfoOrDefault(OAuthInfo oAuthInfo) {
    return userRepository.findByOauthInfo(oAuthInfo.getOauthProvider(),
            oAuthInfo.getOauthProviderId())
        .orElse(
            new User(oAuthInfo)
        );
  }

  public NotificationInfo getNotificationInfoByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(
            NOT_EXIST_USER,
            String.format(NOT_EXIST_USER_MESSAGE, userId))
        ).getNotificationInfo();
  }

  public List<User> getUsersByRoom(ChattingRoom room) {
    return userRepository.findAllByChattingRoom(room);
  }

}
