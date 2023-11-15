package coffeemeet.server.chatting.current.presentation.dto;

import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface ChatsHTTP permits ChatsHTTP.Response {

  record Chat(
      Long messageId,
      String nickname,
      String content,
      String imageUrl,
      LocalDateTime createdAt
  ) {

    public static Chat from(ChattingDto.Response response) {
      return new Chat(response.messageId(),
          response.nickname(),
          response.content(),
          response.imageUrl(),
          response.createdAt()
      );
    }

  }

  record Response(List<Chat> chats) implements ChatsHTTP {

    public static Response from(List<ChattingDto.Response> responses) {
      List<Chat> chatList = responses.stream()
          .map(Chat::from)
          .toList();
      return new Response(chatList);
    }

  }

}
