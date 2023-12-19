package coffeemeet.server.chatting.history.service.dto;

import java.util.List;

public record ChattingHistoryListDto(
    List<ChattingHistory> contents,
    boolean hasNext
) {

  public static ChattingHistoryListDto of(List<ChattingHistory> contents,
      boolean hasNext) {
    return new ChattingHistoryListDto(contents, hasNext);
  }

}
