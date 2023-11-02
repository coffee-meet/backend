package coffeemeet.server.user.implement;

import coffeemeet.server.user.domain.Interest;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.infrastructure.InterestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterestQuery {

  private final InterestRepository interestRepository;

  public List<Keyword> getKeywordsByUserId(Long userId) {
    return interestRepository.findAllByUserId(userId).stream()
        .map(Interest::getKeyword)
        .toList();
  }

  public List<Interest> findAllByUserId(long userId) {
    return interestRepository.findAllByUserId(userId);
  }

}
