package coffeemeet.server.chatting.current.implement;

import static coffeemeet.server.chatting.exception.ChattingErrorCode.FORBIDDEN_EXIT_REQUEST;

import coffeemeet.server.common.execption.ForbiddenException;
import coffeemeet.server.user.domain.User;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class ChattingRoomUserValidator {

  private static final String INVALID_EXIT_REQUEST_MESSAGE = "채팅방에 속하지 않은 사용자(%s)의 채팅방 나가기 요청";

  public void validateUserInChattingRoom(Long requestUserId, List<User> chattingRoomUsers) {
    if (chattingRoomUsers.stream().noneMatch(user -> Objects.equals(user.getId(), requestUserId))) {
      throw new ForbiddenException(FORBIDDEN_EXIT_REQUEST,
          String.format(INVALID_EXIT_REQUEST_MESSAGE, requestUserId));
    }
  }

}
