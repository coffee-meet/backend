package coffeemeet.server.common.fixture;

import static org.instancio.Select.field;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.domain.ChattingSession;
import coffeemeet.server.chatting.current.presentation.dto.ChattingCustomSlice;
import coffeemeet.server.chatting.current.presentation.dto.ChattingRoomStatusHTTP;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import coffeemeet.server.chatting.current.service.dto.ChattingRoomStatusDto;
import coffeemeet.server.chatting.history.domain.ChattingMessageHistory;
import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.presentation.dto.ChattingHistoryCustomSlice;
import coffeemeet.server.chatting.history.presentation.dto.ChattingRoomHistoriesHTTP;
import coffeemeet.server.chatting.history.service.dto.ChattingHistoryListDto;
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

  public static ChattingCustomSlice.Response chatsHTTPResponse(ChattingListDto chats) {
    return Instancio.of(ChattingCustomSlice.Response.class)
        .set(field(ChattingCustomSlice.Response::chats), chats.contents())
        .set(field(ChattingCustomSlice.Response::hasNext), chats.hasNext())
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

  public static ChattingRoomStatusDto chatRoomStatusDto() {
    return Instancio.of(ChattingRoomStatusDto.class)
        .create();
  }

  public static ChattingRoomStatusHTTP.Response chatRoomStatusHTTPResponse(
      ChattingRoomStatusDto chattingRoomStatusDto) {
    return ChattingRoomStatusHTTP.Response.from(chattingRoomStatusDto);
  }

  public static ChattingHistoryListDto chattingMessageHistoryListDto() {
    return Instancio.of(ChattingHistoryListDto.class)
        .create();
  }

  public static ChattingHistoryCustomSlice.Response chattingMessageHistoriesHTTPResponse(
      ChattingHistoryListDto chattingHistoryListDto) {
    return Instancio.of(ChattingHistoryCustomSlice.Response.class)
        .set(field(ChattingHistoryCustomSlice.Response::chatHistories),
            chattingHistoryListDto.contents())
        .set(field(ChattingHistoryCustomSlice.Response::hasNext),
            chattingHistoryListDto.hasNext())
        .create();
  }

}
