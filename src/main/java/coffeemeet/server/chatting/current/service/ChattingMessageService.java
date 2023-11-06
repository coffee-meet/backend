package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingMessageService {

  private final ChattingMessageCommand chattingMessageCommand;
  private final ChattingRoomQuery chattingRoomQuery;
  private final UserQuery userQuery;

  public void createChattingMessage(Long roomId, String content, Long userId) {
    User user = userQuery.getUserById(userId);
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(roomId);
    chattingMessageCommand.saveChattingMessage(content, chattingRoom, user);
  }

}
