package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingSession;
import coffeemeet.server.chatting.current.domain.repository.ChattingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingSessionCommand {

  private final ChattingSessionRepository<ChattingSession, String> chattingSessionRepository;

  public void connect(String sessionId, Long userId) {
    chattingSessionRepository.save(new ChattingSession(sessionId, userId));
  }

  public void disconnect(String sessionId) {
    chattingSessionRepository.deleteById(sessionId);
  }

}
