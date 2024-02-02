package coffeemeet.server.user.implement;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.domain.UserStatus;
import coffeemeet.server.user.infrastructure.InterestRepository;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class UserCommand {

  private final UserRepository userRepository;
  private final UserQuery userQuery;
  private final DuplicatedNicknameValidator duplicatedNicknameValidator;
  private final InterestRepository interestRepository;

  public Long saveUser(User user) {
    return userRepository.save(user).getId();
  }

  public void updateUser(User user) {
    userRepository.save(user);
  }

  public void deleteUser(Long userId) {
    interestRepository.deleteById(userId);
    userRepository.deleteById(userId);
  }

  public void updateUserInfo(Long userId, String nickname) {
    User user = userQuery.getUserById(userId);
    duplicatedNicknameValidator.validateExceptUserId(nickname, user.getId());
    user.updateNickname(nickname);
  }

  public void assignUsersToChattingRoom(Set<Long> matchedUserIds, ChattingRoom chattingRoom) {
    Set<User> users = userQuery.getUsersByIdSet(matchedUserIds);
    users.forEach(user -> user.completeMatching(chattingRoom));
  }

  public void registerOrUpdateNotificationToken(Long userId, String token) {
    User user = userQuery.getUserById(userId);
    user.updateNotificationInfo(
        NotificationInfo.createApprovedNotificationInfo(token, LocalDateTime.now()));
  }

  public void unsubscribeNotification(Long userId) {
    User user = userQuery.getUserById(userId);
    user.updateNotificationInfo(NotificationInfo.createRefusedNotificationInfo());
  }

  public void enterToChattingRoom(Long userId) {
    User user = userQuery.getUserById(userId);
    user.enterChattingRoom();
  }

  public void exitChattingRoom(Long userId) {
    User user = userQuery.getUserById(userId);
    if (user.getUserStatus() == UserStatus.IDLE) {
      return;
    }
    user.exitChattingRoom();
  }

  public void setToIdle(Long userId) {
    User user = userQuery.getUserById(userId);
    user.setIdleStatus();
  }

  public void setToMatching(Long userId) {
    User user = userQuery.getUserById(userId);
    user.matching();
  }

}
