package coffeemeet.server.user.service.cq;

import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

  public void updateUserInfo(Long userId, String nickname) {
    User user = userQuery.getUserById(userId);
    userQuery.hasDuplicatedNickname(nickname);
    user.updateNickname(nickname);
  }

}
