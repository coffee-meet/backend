package coffeemeet.server.chatting.current.presentation;

import coffeemeet.server.chatting.current.presentation.dto.ChatsHTTP;
import coffeemeet.server.chatting.current.service.ChattingRoomService;
import coffeemeet.server.chatting.current.service.dto.ChattingDto.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatting/rooms")
public class ChattingRoomController {

  private final ChattingRoomService chattingRoomService;

  @GetMapping("/{roomId}")
  public ResponseEntity<ChatsHTTP.Response> viewChatRoomMessages(
      @PathVariable Long roomId,
      @RequestParam(defaultValue = "0") Long firstMessageId,
      @RequestParam(defaultValue = "50") int pageSize) {
    List<Response> responses = chattingRoomService.searchMessages(roomId, firstMessageId, pageSize);
    return ResponseEntity.ok(ChatsHTTP.Response.from(responses));
  }

}
