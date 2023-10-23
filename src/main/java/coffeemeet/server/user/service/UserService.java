package coffeemeet.server.user.service;

import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.dto.MyProfileResponse;
import coffeemeet.server.user.dto.UserProfileResponse;
import coffeemeet.server.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final InterestRepository interestRepository;

  @Transactional
  public void updateBusinessCardUrl(Long userId, String businessCardUrl) {
    User user = userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);
    user.updateBusinessCardUrl(businessCardUrl);
  }

  public UserProfileResponse findUserProfile(String nickname) {
    User user = userRepository.findUserByProfileNickname(nickname)
        .orElseThrow(IllegalArgumentException::new);

    List<Interest> interests = interestRepository.findAllByUserId(user.getId());
    return UserProfileResponse.from(user, interests);
  }

  public MyProfileResponse findMyProfile(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);

    List<Interest> interests = interestRepository.findAllByUserId(userId);
    return MyProfileResponse.from(user, interests);
  }

}
