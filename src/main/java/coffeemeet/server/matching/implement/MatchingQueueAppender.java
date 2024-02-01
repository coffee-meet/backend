package coffeemeet.server.matching.implement;

import coffeemeet.server.user.implement.UserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MatchingQueueAppender {

  private final MatchingQueueCommand matchingQueueCommand;
  private final UserCommand userCommand;

  @Transactional
  public void append(String companyName, Long userId) {
    matchingQueueCommand.enqueueUserByCompanyName(companyName, userId);
    userCommand.setToMatching(userId);
  }
}
