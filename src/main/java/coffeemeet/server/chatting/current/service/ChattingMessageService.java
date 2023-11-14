package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.chatting.exception.ChattingErrorCode;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingMessageService {

  private static final String SOCKET_SESSION_NOT_FOUND_MESSAGE = "소켓 연결 정보가 없습니다.";

  private final Map<String, Long> sessions = new ConcurrentHashMap<>();
  private final ChattingMessageCommand chattingMessageCommand;
  private final ChattingRoomQuery chattingRoomQuery;
  private final UserQuery userQuery;
  private FCMNotificationSender fcmNotificationSender;

  public ChattingDto.Response chatting(String sessionId, Long roomId, String content) {
    Long userId = Optional.of(sessions.get(sessionId))
        .orElseThrow(() ->
            new NotFoundException(
                ChattingErrorCode.SOCKET_SESSION_NOT_FOUND,
                SOCKET_SESSION_NOT_FOUND_MESSAGE
            ));
    // TODO: 11/13/23 dev pull 받고, 알람 이어서 작성
    User user = userQuery.getUserById(userId);
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(roomId);
    ChattingMessage chattingMessage = chattingMessageCommand.saveChattingMessage(content,
        chattingRoom, user);
    return ChattingDto.Response.of(user, chattingMessage);
  }

  public void storeSocketSession(String sessionId, String userId) {
    sessions.put(sessionId, Long.valueOf(userId));
  }

  public void expireSocketSession(String sessionId) {
    sessions.remove(sessionId);
  }

}
