package coffeemeet.server.interest.service;

import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestService {

  private final InterestRepository interestRepository;
  private final UserRepository userRepository;

  @Transactional
  public void updateInterests(Long userId, List<Keyword> interests) {

    for (Keyword interest : interests) {
      try {
        Keyword.valueOf(interest.name());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("유효한 관심사가 아닙니다.");
      }
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

    List<Interest> currentInterests = interestRepository.findAllByUserId(userId);
    interestRepository.deleteAll(currentInterests);

    for (Keyword keyword : interests) {
      Interest newInterest = new Interest(keyword, user);
      interestRepository.save(newInterest);
    }
  }

}
