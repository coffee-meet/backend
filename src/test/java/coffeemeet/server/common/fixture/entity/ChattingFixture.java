package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.presentation.dto.ChatsHTTP;
import coffeemeet.server.chatting.current.presentation.dto.ChatsHTTP.Chat;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.user.domain.User;
import java.util.List;
import org.instancio.Instancio;

public class ChattingFixture {

  public static ChattingMessage chattingMessage(String content) {
    return Instancio.of(ChattingMessage.class)
        .set(field(ChattingMessage::getMessage), content)
        .create();
  }

  public static List<ChattingMessage> chattingMessages(ChattingRoom room, int size) {
    return Instancio.ofList(ChattingMessage.class).size(size)
        .create();
  }

  public static List<ChattingMessage> chattingMessages(ChattingRoom room, User user, int size) {
    return Instancio.ofList(ChattingMessage.class).size(size)
        .set(field(ChattingMessage::getChattingRoom), room)
        .set(field(ChattingMessage::getUser), user)
        .create();
  }

  public static ChattingRoom chattingRoom() {
    return Instancio.of(ChattingRoom.class)
        .create();
  }

  public static List<ChattingDto.Response> chattingDtoResponse(int size) {
    return Instancio.ofList(ChattingDto.Response.class).size(size)
        .create();
  }

  public static ChatsHTTP.Response chatsHTTPResponse(List<Chat> chats) {
    return Instancio.of(ChatsHTTP.Response.class)
        .set(field(ChatsHTTP.Response::chats), chats)
        .create();
  }

  public static List<ChattingMessageHistory> chattingMessageHistories(int size) {
    return Instancio.ofList(ChattingMessageHistory.class).size(size)
        .create();
  }

  public static ChattingRoomHistory chattingRoomHistory() {
    return Instancio.of(ChattingRoomHistory.class)
        .create();
  }

  public static List<UserChattingHistory> userChattingHistories(int size) {
    return Instancio.ofList(UserChattingHistory.class).size(size)
        .create();
  }

}
