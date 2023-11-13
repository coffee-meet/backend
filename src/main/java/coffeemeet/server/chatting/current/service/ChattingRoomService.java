package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingMessageQuery;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.chatting.current.implement.ChattingRoomQuery;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.chatting.current.service.dto.ChattingDto.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {

  private final ChattingRoomCommand chattingRoomCommand;
  private final ChattingRoomQuery chattingRoomQuery;
  private final ChattingMessageQuery chattingMessageQuery;

  public Long createChattingRoom() {
    return chattingRoomCommand.createChattingRoom().getId();
  }

  public List<Response> searchMessages(Long roomId, Long firstMessageId, int pageSize) {
    ChattingRoom chattingRoom = chattingRoomQuery.getChattingRoomById(roomId);
    return chattingMessageQuery.findMessages(chattingRoom, firstMessageId, pageSize)
        .stream()
        .map(message -> ChattingDto.Response.of(message.getUser(), message))
        .toList();
  }

}
