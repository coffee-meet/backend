package coffeemeet.server.chatting.current.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ChatsHTTP {

  public record Response(
      List<ChattingDto> chats,
      boolean hasNext
  ) {

    public static Response from(ChattingListDto chattingListDto) {
      return new Response(chattingListDto.contents(), chattingListDto.hasNext());
    }

  }

}
