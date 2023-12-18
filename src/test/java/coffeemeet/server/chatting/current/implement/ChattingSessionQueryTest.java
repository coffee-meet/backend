package coffeemeet.server.chatting.current.implement;

import static coffeemeet.server.common.fixture.entity.ChattingFixture.chattingSession;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingSession;
import coffeemeet.server.chatting.current.infrastructure.ChattingSessionRepository;
import coffeemeet.server.common.execption.NotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingSessionQueryTest {

  @InjectMocks
  private ChattingSessionQuery chattingSessionQuery;

  @Mock
  private ChattingSessionRepository<ChattingSession, String> chattingSessionRepository;

  @Test
  @DisplayName("세션 아이디로 유저 아이디를 불러올 수 있다.")
  void getUserIdByIdTest() {
    // given
    ChattingSession chattingSession = chattingSession();

    given(chattingSessionRepository.findById(chattingSession.sessionId()))
        .willReturn(Optional.of(chattingSession));

    // when
    Long userId = chattingSessionQuery.getUserIdById(chattingSession.sessionId());

    // then
    assertThat(userId).isEqualTo(chattingSession.userId());
  }

  @Test
  @DisplayName("세션 아이디로 유저 아이디를 불러올 수 없다면, 예외가 발생할 수 있다.")
  void getUserIdByIdTest_NotFoundException() {
    // given
    ChattingSession chattingSession = chattingSession();

    given(chattingSessionRepository.findById(chattingSession.sessionId()))
        .willReturn(Optional.empty());

    // when, then
    String invalidSession = chattingSession.sessionId();
    assertThatThrownBy(() -> chattingSessionQuery.getUserIdById(invalidSession))
        .isInstanceOf(NotFoundException.class);
  }

}
