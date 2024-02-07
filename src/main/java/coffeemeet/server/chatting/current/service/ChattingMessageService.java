package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.implement.ChattingSessionCommand;
import coffeemeet.server.chatting.current.implement.ChattingSessionQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingMessageService {

  private final ChattingSessionQuery chattingSessionQuery;
  private final ChattingSessionCommand chattingSessionCommand;
  private final ChattingMessageCommand chattingMessageCommand;
  private final ChattingRoomQuery chattingRoomQuery;
  private final UserCommand userCommand;

  public ChattingDto chat(String sessionId, Long roomId, String content) {
    User user = chattingSessionQuery.getUserById(sessionId);
    ChattingRoom room = chattingRoomQuery.getChattingRoomById(roomId);
    ChattingMessage chattingMessage = chattingMessageCommand.createChattingMessage(content,
        room, user);
    return ChattingDto.of(user, chattingMessage);
  }

  public void storeSocketSession(String sessionId, String userId) {
    userCommand.enterToChattingRoom(Long.valueOf(userId));
    chattingSessionCommand.connect(sessionId, Long.parseLong(userId));
  }

  public void expireSocketSession(String sessionId) {
    Long userId = chattingSessionQuery.getUserIdById(sessionId);
    userCommand.exitChattingRoom(userId);
    chattingSessionCommand.disconnect(sessionId);
  }

}
