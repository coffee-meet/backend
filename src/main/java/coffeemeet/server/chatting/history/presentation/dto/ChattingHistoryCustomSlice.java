package coffeemeet.server.chatting.history.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.chatting.history.service.dto.ChattingHistory;
import coffeemeet.server.chatting.history.service.dto.ChattingHistoryListDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ChattingHistoryCustomSlice {

  public record Response(
      List<ChattingHistory> chatHistories,
      boolean hasNext) {

    public static Response from(ChattingHistoryListDto chattingHistoryListDto) {
      return new Response(chattingHistoryListDto.contents(),
          chattingHistoryListDto.hasNext());
    }

  }

}
