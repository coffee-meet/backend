package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.domain.ChattingSession;
import coffeemeet.server.chatting.current.presentation.dto.ChatRoomStatusHTTP;
import coffeemeet.server.chatting.current.presentation.dto.ChatsHTTP;
import coffeemeet.server.chatting.current.service.dto.ChatRoomStatusDto;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.presentation.dto.ChattingMessageHistoriesHTTP;
import coffeemeet.server.chatting.history.presentation.dto.ChattingRoomHistoriesHTTP;
import coffeemeet.server.chatting.history.service.dto.ChattingMessageHistoryListDto;
import coffeemeet.server.chatting.history.service.dto.ChattingRoomHistoryDto;
import coffeemeet.server.user.domain.User;
import java.util.List;
import java.util.Set;
import org.instancio.Instancio;

public class ChattingFixture {

  public static ChattingMessage chattingMessage(String content) {
    return Instancio.of(ChattingMessage.class)
        .set(field(ChattingMessage::getMessage), content)
        .create();
  }

  public static List<ChattingMessage> chattingMessages(int size) {
    return Instancio.ofList(ChattingMessage.class).size(size)
        .generate(field(ChattingMessage::getId), gen -> gen.longSeq().start(1L))
        .create();
  }

  public static List<ChattingMessage> chattingMessages(ChattingRoom room, User user, int size) {
    return Instancio.ofList(ChattingMessage.class).size(size)
        .set(field(ChattingMessage::getChattingRoom), room)
        .set(field(ChattingMessage::getUser), user)
        .generate(field(ChattingMessage::getId), gen -> gen.longSeq().start(1L))
        .create();
  }

  public static ChattingRoom chattingRoom() {
    return Instancio.of(ChattingRoom.class)
        .create();
  }

  public static ChattingListDto chattingListDto() {
    return Instancio.of(ChattingListDto.class)
        .create();
  }

  public static List<ChattingRoomHistoryDto> chattingRoomHistoryDtoResponses(int size) {
    return Instancio.ofList(ChattingRoomHistoryDto.class).size(size)
        .create();
  }

  public static ChatsHTTP.Response chatsHTTPResponse(ChattingListDto chats) {
    return Instancio.of(ChatsHTTP.Response.class)
        .set(field(ChatsHTTP.Response::chats), chats.contents())
        .set(field(ChatsHTTP.Response::hasNext), chats.hasNext())
        .create();
  }

  public static ChattingRoomHistoriesHTTP.Response chattingRoomHistoriesHTTP(
      List<ChattingRoomHistoryDto> chatRoomHistories) {
    return Instancio.of(ChattingRoomHistoriesHTTP.Response.class)
        .set(field(ChattingRoomHistoriesHTTP.Response::chatRoomHistories), chatRoomHistories)
        .create();
  }

  public static List<ChattingMessageHistory> chattingMessageHistories(int size) {
    return Instancio.ofList(ChattingMessageHistory.class).size(size)
        .generate(field(ChattingMessageHistory::getId), gen -> gen.longSeq().start(1L))
        .create();
  }

  public static List<ChattingMessageHistory> chattingMessageHistories(
      ChattingRoomHistory chattingRoomHistory, User user, int size) {
    return Instancio.ofList(ChattingMessageHistory.class).size(size)
        .set(field(ChattingMessageHistory::getChattingRoomHistory), chattingRoomHistory)
        .set(field(ChattingMessageHistory::getUser), user)
        .generate(field(ChattingMessageHistory::getId), gen -> gen.longSeq().start(1L))
        .create();
  }

  public static ChattingRoomHistory chattingRoomHistory() {
    return Instancio.of(ChattingRoomHistory.class)
        .create();
  }

  public static List<UserChattingHistory> userChattingHistories(int size) {
    return Instancio.ofList(UserChattingHistory.class).size(size)
        .generate(field(UserChattingHistory::getId), gen -> gen.longSeq().start(1L))
        .create();
  }

  public static UserChattingHistory userChattingHistory(User user,
      ChattingRoomHistory chattingRoomHistory) {
    return Instancio.of(UserChattingHistory.class)
        .set(field(UserChattingHistory::getUser), user)
        .set(field(UserChattingHistory::getChattingRoomHistory), chattingRoomHistory)
        .create();
  }

  public static Set<ChattingRoom> chattingRoom(int size) {
    return Instancio.ofSet(ChattingRoom.class).size(size)
        .create();
  }

  public static ChattingSession chattingSession() {
    return Instancio.create(ChattingSession.class);
  }

  public static ChatRoomStatusDto chatRoomStatusDto() {
    return Instancio.of(ChatRoomStatusDto.class)
        .create();
  }

  public static ChatRoomStatusHTTP.Response chatRoomStatusHTTPResponse(
      ChatRoomStatusDto chatRoomStatusDto) {
    return ChatRoomStatusHTTP.Response.from(chatRoomStatusDto);
  }

  public static ChattingMessageHistoryListDto chattingMessageHistoryListDto() {
    return Instancio.of(ChattingMessageHistoryListDto.class)
        .create();
  }

  public static ChattingMessageHistoriesHTTP.Response chattingMessageHistoriesHTTPResponse(
      ChattingMessageHistoryListDto chattingMessageHistoryListDto) {
    return Instancio.of(ChattingMessageHistoriesHTTP.Response.class)
        .set(field(ChattingMessageHistoriesHTTP.Response::chatHistories),
            chattingMessageHistoryListDto.contents())
        .set(field(ChattingMessageHistoriesHTTP.Response::hasNext),
            chattingMessageHistoryListDto.hasNext())
        .create();
  }

}
