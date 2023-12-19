package coffeemeet.server.chatting.current.service.dto;

import java.util.List;

public record ChattingListDto(
    List<Chatting> contents,
    boolean hasNext
) {

  public static ChattingListDto of(List<Chatting> contents, boolean hasNext) {
    return new ChattingListDto(contents, hasNext);
  }

}
