package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
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

  public ChattingDto.Response chatting(Long roomId, String content, Long userId) {
    User user = userQuery.getUserById(userId);
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(roomId);
    ChattingMessage chattingMessage = chattingMessageCommand.saveChattingMessage(content,
        chattingRoom, user);
    return ChattingDto.Response.of(user, chattingMessage);
  }

}
