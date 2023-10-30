package coffeemeet.server.interest.service.cq;

import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestCommand {

  private final InterestRepository interestRepository;

  public List<Interest> findAllByUserId(long userId) {
    return interestRepository.findAllByUserId(userId);
  }

  public void saveAll(List<Keyword> keywords, User user) {
    List<Interest> interests = keywords.stream()
        .map(keyword -> new Interest(keyword, user))
        .toList();
    interestRepository.saveAll(interests);
  }

  public void updateInterests(User user, List<Keyword> keywords) {
    List<Interest> currentInterests = findAllByUserId(user.getId());
    deleteAll(currentInterests);
    saveAll(keywords, user);
  }

  public void deleteAll(List<Interest> interests) {
    interestRepository.deleteAllInBatch(interests);
  }

}
