package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingSession;
import coffeemeet.server.chatting.current.infrastructure.ChattingSessionRepository;
import coffeemeet.server.chatting.exception.ChattingErrorCode;
import coffeemeet.server.common.execption.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingSessionQuery {

  private static final String SOCKET_SESSION_NOT_FOUND_MESSAGE = "소켓 연결 정보가 없습니다.";

  private final ChattingSessionRepository<ChattingSession, String> chattingSessionRepository;

  public Long getUserIdById(String id) {
    log.info("getUserIdById 함수 시작");
    return chattingSessionRepository.findById(id).orElseThrow(
        () -> new NotFoundException(ChattingErrorCode.SOCKET_SESSION_NOT_FOUND,
            SOCKET_SESSION_NOT_FOUND_MESSAGE)
    ).userId();
  }

}
