package coffeemeet.server.chatting.history.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.chatting.history.service.dto.ChattingRoomHistoryDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ChattingRoomHistoriesHTTP {

  public record Response(List<ChattingRoomHistoryDto> chatRoomHistories) {

    public static Response from(List<ChattingRoomHistoryDto> responses) {
      return new Response(responses);
    }

  }

}
