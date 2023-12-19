package coffeemeet.server.chatting.history.service.dto;

import java.util.List;

public record ChattingMessageHistoryListDto(
    List<ChattingMessageHistory> contents,
    boolean hasNext
) {

  public static ChattingMessageHistoryListDto of(List<ChattingMessageHistory> contents,
      boolean hasNext) {
    return new ChattingMessageHistoryListDto(contents, hasNext);
  }

}
