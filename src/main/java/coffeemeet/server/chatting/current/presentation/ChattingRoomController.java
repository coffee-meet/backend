package coffeemeet.server.chatting.current.presentation;

import coffeemeet.server.chatting.current.presentation.dto.ChattingCustomSlice;
import coffeemeet.server.chatting.current.presentation.dto.ChattingRoomStatusHTTP;
import coffeemeet.server.chatting.current.service.ChattingRoomService;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import coffeemeet.server.chatting.current.service.dto.ChattingRoomStatusDto;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.domain.AuthInfo;
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
      @Login AuthInfo authInfo,
      @PathVariable Long roomId,
      @RequestParam Long lastMessageId,
      @RequestParam(defaultValue = "50") int pageSize) {
    ChattingListDto responses = chattingRoomService.searchMessages(authInfo.userId(), roomId,
        lastMessageId,
        pageSize);
    return ResponseEntity.ok(ChattingCustomSlice.Response.from(responses));
  }

  @DeleteMapping("/{roomId}")
  public ResponseEntity<Void> exitChattingRoom(
      @Login AuthInfo authInfo,
      @PathVariable Long roomId,
      @RequestParam Long chattingRoomLastMessageId
  ) {
    chattingRoomService.exitChattingRoom(authInfo.userId(), roomId, chattingRoomLastMessageId);
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
