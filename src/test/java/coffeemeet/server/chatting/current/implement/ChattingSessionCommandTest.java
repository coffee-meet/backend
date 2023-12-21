package coffeemeet.server.chatting.current.implement;

import static coffeemeet.server.common.fixture.ChattingFixture.chattingSession;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.chatting.current.domain.ChattingSession;
import coffeemeet.server.chatting.current.infrastructure.ChattingSessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingSessionCommandTest {

  @InjectMocks
  private ChattingSessionCommand chattingSessionCommand;

  @Mock
  private ChattingSessionRepository<ChattingSession, String> chattingSessionRepository;

  @Test
  @DisplayName("세션을 연결할 수 있다..")
  void connect() {
    // given
    ChattingSession chattingSession = chattingSession();

    // when
    chattingSessionCommand.connect(chattingSession.sessionId(), chattingSession.userId());

    // then
    then(chattingSessionRepository).should(only()).save(chattingSession);
  }

  @Test
  @DisplayName("세션 연결을 끊을 수 있다.")
  void disconnect() {
    // given
    String sessionId = "sessionId";

    // when
    chattingSessionCommand.disconnect(sessionId);

    // then
    then(chattingSessionRepository).should(only()).deleteById(sessionId);
  }

}
