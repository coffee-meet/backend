package coffeemeet.server.chatting.history.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.chatting.history.service.dto.ChattingHistory;
import coffeemeet.server.chatting.history.service.dto.ChattingMessageHistoryListDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ChattingMessageHistoriesHTTP {

  public record Response(
      List<ChattingHistory> chatHistories,
      boolean hasNext) {

    public static Response from(ChattingMessageHistoryListDto chattingMessageHistoryListDto) {
      return new Response(chattingMessageHistoryListDto.contents(),
          chattingMessageHistoryListDto.hasNext());
    }

  }

}
