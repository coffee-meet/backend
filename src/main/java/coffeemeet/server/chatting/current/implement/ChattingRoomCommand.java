package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingRoomRepository;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingRoomCommand {

  private final ChattingRoomRepository chattingRoomRepository;
  private final ChattingMessageCommand chattingMessageCommand;
  private final UserQuery userQuery;

  public ChattingRoom createChattingRoom() {
    return chattingRoomRepository.save(new ChattingRoom());
  }

  public void removeChattingRoom(ChattingRoom chattingRoom) {
    chattingMessageCommand.deleteAllChattingMessagesBy(chattingRoom);
    List<User> users = userQuery.getUsersByRoom(chattingRoom);
    users.forEach(User::deleteChattingRoom);
    chattingRoomRepository.deleteById(chattingRoom.getId());
  }

}
