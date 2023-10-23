package coffeemeet.server.user.service;

import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
        .orElseThrow(EntityNotFoundException::new);  // 에러 어떻게 처리할꺼임?
    user.updateBusinessCardUrl(businessCardUrl);
  }

}
