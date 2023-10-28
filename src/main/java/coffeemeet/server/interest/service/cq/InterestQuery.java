package coffeemeet.server.interest.service.cq;

import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.repository.InterestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterestQuery {

  private final InterestRepository interestRepository;

  public List<Keyword> getKeywordsByUserId(Long userId) {
    return interestRepository.findAllByUserId(userId).stream()
        .map(Interest::getKeyword)
        .toList();
  }

}
