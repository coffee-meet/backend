package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingSession;
import coffeemeet.server.chatting.current.infrastructure.ChattingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
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
