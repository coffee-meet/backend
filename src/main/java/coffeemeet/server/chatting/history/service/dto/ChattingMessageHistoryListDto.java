package coffeemeet.server.chatting.history.service.dto;

import java.util.List;

public record ChattingMessageHistoryListDto(
    List<ChattingMessageHistoryDto> contents,
    boolean hasNext
) {

  public static ChattingMessageHistoryListDto of(List<ChattingMessageHistoryDto> contents,
      boolean hasNext) {
    return new ChattingMessageHistoryListDto(contents, hasNext);
  }

}
