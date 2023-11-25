package coffeemeet.server.chatting.current.infrastructure;

import coffeemeet.server.chatting.current.domain.ChattingSession;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryChattingSessionRepository implements
    ChattingSessionRepository<ChattingSession, String> {

  private final Map<String, ChattingSession> sessions = new ConcurrentHashMap<>();

  @Override
  public ChattingSession save(ChattingSession session) {
    sessions.put(session.sessionId(), session);
    return session;
  }

  @Override
  public Optional<ChattingSession> findById(String id) {
    return Optional.ofNullable(sessions.get(id));
  }

  @Override
  public void deleteById(String id) {
    sessions.remove(id);
  }

}
