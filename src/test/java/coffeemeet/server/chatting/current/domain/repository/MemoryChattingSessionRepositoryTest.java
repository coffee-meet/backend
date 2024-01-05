package coffeemeet.server.chatting.current.domain.repository;

import static coffeemeet.server.common.fixture.ChattingFixture.chattingSession;
import static org.assertj.core.api.Assertions.assertThat;

import coffeemeet.server.chatting.current.domain.ChattingSession;
import coffeemeet.server.chatting.current.domain.repository.MemoryChattingSessionRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemoryChattingSessionRepositoryTest {

  private MemoryChattingSessionRepository repository;

  @BeforeEach
  void setUp() {
    repository = new MemoryChattingSessionRepository();
  }


  @Test
  @DisplayName("세션을 저장할 수 있다.")
  void save() {
    // given
    ChattingSession session = chattingSession();

    // when
    ChattingSession saved = repository.save(session);

    // then
    assertThat(saved).isEqualTo(session);
  }

  @Test
  @DisplayName("세션을 저장할 수 있다.")
  void findById() {
    // given
    ChattingSession session = chattingSession();

    repository.save(session);

    // when
    Optional<ChattingSession> found = repository.findById(session.sessionId());

    // then
    assertThat(found)
        .isPresent()
        .contains(session);
  }

  @Test
  @DisplayName("세션을 삭제할 수 있다.")
  void deleteById() {
    ChattingSession session = chattingSession();

    repository.save(session);

    // when
    repository.deleteById(session.sessionId());

    // then
    assertThat(repository.findById(session.sessionId())).isEmpty();
  }

}
