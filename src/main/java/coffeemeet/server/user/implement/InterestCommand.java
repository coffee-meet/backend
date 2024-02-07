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
  private final UserQuery userQuery;

  public void saveAll(List<Keyword> keywords, User user) {
    List<Interest> interests = keywords.stream()
        .map(keyword -> new Interest(keyword, user))
        .toList();
    interestRepository.saveAll(interests);
  }

  public void updateInterests(Long userId, List<Keyword> keywords) {
    User user = userQuery.getUserById(userId);
    interestRepository.deleteAllByUserId(userId);
    interestRepository.saveAll(
        keywords.stream()
            .map(keyword -> new Interest(keyword, user))
            .toList()
    );
  }

  public void deleteAll(List<Interest> interests) {
    interestRepository.deleteAllInBatch(interests);
  }

}
