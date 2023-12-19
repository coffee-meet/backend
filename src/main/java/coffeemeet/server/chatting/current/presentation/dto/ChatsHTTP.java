package coffeemeet.server.chatting.current.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.chatting.current.service.dto.Chatting;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ChatsHTTP {

  public record Response(
      List<Chatting> chats,
      boolean hasNext
  ) {

    public static Response from(ChattingListDto chattingListDto) {
      return new Response(chattingListDto.contents(), chattingListDto.hasNext());
    }

  }

}
