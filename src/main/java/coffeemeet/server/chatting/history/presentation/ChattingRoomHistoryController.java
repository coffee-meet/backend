package coffeemeet.server.chatting.history.presentation;

import coffeemeet.server.chatting.history.presentation.dto.ChattingMessageHistoriesHTTP;
import coffeemeet.server.chatting.history.presentation.dto.ChattingRoomHistoriesHTTP;
import coffeemeet.server.chatting.history.service.ChattingRoomHistoryService;
import coffeemeet.server.chatting.history.service.dto.ChattingMessageHistoryDto;
import coffeemeet.server.chatting.history.service.dto.ChattingRoomHistoryDto.Response;
import coffeemeet.server.common.annotation.Login;
import coffeemeet.server.common.domain.AuthInfo;
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
@RequestMapping("/api/v1/chatting/room/histories")
public class ChattingRoomHistoryController {

  private final ChattingRoomHistoryService chattingRoomHistoryService;

  @GetMapping
  public ResponseEntity<ChattingRoomHistoriesHTTP.Response> viewChattingRoomHistories(
      @Login AuthInfo authInfo
  ) {
    List<Response> responses = chattingRoomHistoryService.searchChattingRoomHistories(
        authInfo.userId());
    return ResponseEntity.ok(ChattingRoomHistoriesHTTP.Response.from(responses));
  }

  @GetMapping("/{roomHistoryId}")
  public ResponseEntity<ChattingMessageHistoriesHTTP.Response> viewChattingRoomMessageHistories(
      @PathVariable Long roomHistoryId,
      @RequestParam(defaultValue = "0") Long firstMessageId,
      @RequestParam(defaultValue = "50") int pageSize) {
    List<ChattingMessageHistoryDto.Response> responses = chattingRoomHistoryService.searchChattingMessageHistories(
        roomHistoryId, firstMessageId, pageSize);
    return ResponseEntity.ok(ChattingMessageHistoriesHTTP.Response.from(responses));
  }

}
