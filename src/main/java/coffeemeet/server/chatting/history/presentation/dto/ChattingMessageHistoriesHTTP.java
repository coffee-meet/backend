package coffeemeet.server.chatting.history.presentation.dto;

import coffeemeet.server.chatting.history.service.dto.ChattingMessageHistoryDto;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface ChattingMessageHistoriesHTTP permits ChattingMessageHistoriesHTTP.Response {

  record ChatHistory(
      Long userId,
      Long messageId,
      String nickname,
      String content,
      String profileImageUrl,
      LocalDateTime createdAt
  ) {

    public static ChatHistory from(ChattingMessageHistoryDto.Response response) {
      return new ChatHistory(
          response.userId(),
          response.messageId(),
          response.nickname(),
          response.content(),
          response.profileImageUrl(),
          response.createdAt()
      );
    }

  }

  record Response(List<ChatHistory> chatHistories) implements ChattingMessageHistoriesHTTP {

    public static Response from(List<ChattingMessageHistoryDto.Response> responses) {
      List<ChatHistory> chatHistoryList = responses.stream()
          .map(ChatHistory::from)
          .toList();
      return new Response(chatHistoryList);
    }

  }

}
