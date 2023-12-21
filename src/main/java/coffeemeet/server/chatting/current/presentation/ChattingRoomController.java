package coffeemeet.server.chatting.current.presentation;

import coffeemeet.server.chatting.current.presentation.dto.ChattingCustomSlice;
import coffeemeet.server.chatting.current.presentation.dto.ChattingRoomStatusHTTP;
import coffeemeet.server.chatting.current.service.ChattingRoomService;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import coffeemeet.server.chatting.current.service.dto.ChattingRoomStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  public ResponseEntity<ChattingCustomSlice.Response> viewChattingRoomMessages(
      @PathVariable Long roomId,
      @RequestParam(defaultValue = "0") Long firstMessageId,
      @RequestParam(defaultValue = "50") int pageSize) {
    ChattingListDto responses = chattingRoomService.searchMessages(roomId, firstMessageId,
        pageSize);
    return ResponseEntity.ok(ChattingCustomSlice.Response.from(responses));
  }

  @DeleteMapping("/{roomId}")
  public ResponseEntity<Void> exitChattingRoom(@PathVariable Long roomId) {
    chattingRoomService.deleteChattingRoom(roomId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{roomId}/exist")
  public ResponseEntity<ChattingRoomStatusHTTP.Response> checkChattingRoom(
      @PathVariable Long roomId) {
    ChattingRoomStatusDto chattingRoomStatusDto = chattingRoomService.checkChattingRoomStatus(
        roomId);
    return ResponseEntity.ok(ChattingRoomStatusHTTP.Response.from(chattingRoomStatusDto));
  }

}
