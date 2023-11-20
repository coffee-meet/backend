package coffeemeet.server.chatting.current.presentation.dto;

import static lombok.AccessLevel.PRIVATE;

import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ChatsHTTP {

  record Chat(
      Long userId,
      Long messageId,
      String nickname,
      String content,
      String profileImageUrl,
      LocalDateTime createdAt
  ) {

    public static Chat from(ChattingDto.Response response) {
      return new Chat(
          response.userId(),
          response.messageId(),
          response.nickname(),
          response.content(),
          response.profileImageUrl(),
          response.createdAt()
      );
    }

  }

  public record Response(List<Chat> chats) {

    public static Response from(List<ChattingDto.Response> responses) {
      List<Chat> chatList = responses.stream()
          .map(Chat::from)
          .toList();
      return new Response(chatList);
    }

  }

}
