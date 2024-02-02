package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingSession;
import coffeemeet.server.chatting.current.domain.repository.ChattingSessionRepository;
import coffeemeet.server.chatting.exception.ChattingErrorCode;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChattingSessionQuery {

  private static final String SOCKET_SESSION_NOT_FOUND_MESSAGE = "소켓 연결 정보가 없습니다.";

  private final ChattingSessionRepository<ChattingSession, String> chattingSessionRepository;
  private final UserQuery userQuery;

  public Long getUserIdById(String id) {
    return chattingSessionRepository.findById(id).orElseThrow(
        () -> new NotFoundException(ChattingErrorCode.SOCKET_SESSION_NOT_FOUND,
            SOCKET_SESSION_NOT_FOUND_MESSAGE)
    ).userId();
  }

  public User getUserById(String id) {

    Long userId = chattingSessionRepository.findById(id).orElseThrow(
        () -> new NotFoundException(ChattingErrorCode.SOCKET_SESSION_NOT_FOUND,
            SOCKET_SESSION_NOT_FOUND_MESSAGE)
    ).userId();
    return userQuery.getUserById(userId);
  }

}
