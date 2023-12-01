package coffeemeet.server.chatting.current.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.chatting.current.service.dto.ChatRoomStatusDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ChatRoomStatusHTTP {

  public record Response(
      boolean isExisted
  ) {

    public static ChatRoomStatusHTTP.Response from(ChatRoomStatusDto chatRoomStatusDto) {
      return new Response(chatRoomStatusDto.isExisted());
    }

  }

}
