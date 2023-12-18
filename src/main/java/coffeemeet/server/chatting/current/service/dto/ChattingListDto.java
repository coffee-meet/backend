package coffeemeet.server.chatting.current.service.dto;

import java.util.List;

public record ChattingListDto(
    List<ChattingDto> contents,
    boolean hasNext
) {

  public static ChattingListDto of(List<ChattingDto> contents, boolean hasNext) {
    return new ChattingListDto(contents, hasNext);
  }

}
