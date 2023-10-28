package coffeemeet.server.interest.service.cq;

import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.dto.SignupDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestCommand {

  private final InterestRepository interestRepository;

  public void saveInterests(SignupDto.Request request, User newUser) {
    List<Interest> interests = new ArrayList<>();
    for (Keyword value : request.keywords()) {
      interests.add(new Interest(value, newUser));
    }
    interestRepository.saveAll(interests);
  }

  public void updateInterests(User user, List<Keyword> keywords) {
    List<Interest> currentInterests = interestRepository.findAllByUserId(user.getId());
    interestRepository.deleteAllInBatch(currentInterests);

    List<Interest> interests = keywords.stream()
        .map(keyword -> new Interest(keyword, user))
        .toList();
    interestRepository.saveAll(interests);
  }

}
