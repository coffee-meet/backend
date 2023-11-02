package coffeemeet.server.user.implement;

import coffeemeet.server.user.domain.Interest;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.InterestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class InterestCommand {

  private final InterestRepository interestRepository;
  private final InterestQuery interestQuery;

  public void saveAll(List<Keyword> keywords, User user) {
    List<Interest> interests = keywords.stream()
        .map(keyword -> new Interest(keyword, user))
        .toList();
    interestRepository.saveAll(interests);
  }

  public void updateInterests(User user, List<Keyword> keywords) {
    List<Interest> currentInterests = interestQuery.findAllByUserId(user.getId());
    List<Keyword> currentKeywords = currentInterests.stream()
        .map(Interest::getKeyword)
        .toList();

    if (!currentKeywords.equals(keywords)) {
      deleteAll(currentInterests);
      saveAll(keywords, user);
    }
  }

  public void deleteAll(List<Interest> interests) {
    interestRepository.deleteAllInBatch(interests);
  }

}
