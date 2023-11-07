package coffeemeet.server.user.implement;

import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.InterestRepository;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class UserCommand {

  private final UserRepository userRepository;
  private final UserQuery userQuery;
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

  public void updateUserInfo(User user, String nickname) {
    userQuery.hasDuplicatedNickname(nickname);
    user.updateNickname(nickname);
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

}
