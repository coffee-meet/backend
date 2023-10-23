package coffeemeet.server.user.service;

import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public void updateBusinessCardUrl(Long userId, String businessCardUrl) {
    User user = userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);
    user.updateBusinessCardUrl(businessCardUrl);
  }

}
