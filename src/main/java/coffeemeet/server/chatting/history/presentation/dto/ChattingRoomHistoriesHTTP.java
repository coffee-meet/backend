package coffeemeet.server.chatting.history.presentation.dto;

import coffeemeet.server.chatting.history.service.dto.ChattingRoomHistoryDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface ChattingRoomHistoriesHTTP permits ChattingRoomHistoriesHTTP.Response {

  record ChatRoomHistory(
      Long roomId,
      String roomName,
      List<String> users,
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
      LocalDateTime createdAt
  ) {

    public static ChatRoomHistory from(ChattingRoomHistoryDto.Response response) {
      return new ChatRoomHistory(
          response.roomId(),
          response.roomName(),
          response.users(),
          response.createdAt()
      );
    }

  }

  record Response(List<ChatRoomHistory> chatRoomHistories) implements ChattingRoomHistoriesHTTP {

    public static Response from(List<ChattingRoomHistoryDto.Response> responses) {
      List<ChatRoomHistory> roomHistories = responses.stream()
          .map(ChatRoomHistory::from)
          .toList();
      return new Response(roomHistories);
    }

  }

}
