package coffeemeet.server.chatting.current.presentation;

import coffeemeet.server.chatting.current.presentation.dto.ChattingStomp;
import coffeemeet.server.chatting.current.service.ChattingMessageService;
import coffeemeet.server.chatting.current.service.dto.ChattingDto;
import coffeemeet.server.common.annotation.PerformanceMeasurement;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChattingMessageController {

  private final SimpMessageSendingOperations simpMessageSendingOperations;
  private final ChattingMessageService chattingMessageService;

  @EventListener(SessionConnectEvent.class)
  public void onConnect(SessionConnectEvent event) {
    String sessionId = String.valueOf(event.getMessage().getHeaders().get("simpSessionId"));
    String userId = String.valueOf(event.getMessage().getHeaders().get("nativeHeaders"))
        .split("userId=\\[")[1].split("]")[0];
    chattingMessageService.storeSocketSession(sessionId, userId);
  }

  @PerformanceMeasurement
  @EventListener(SessionDisconnectEvent.class)
  public void onDisconnect(SessionDisconnectEvent event) {
    log.info("세션 제거하기 시작 : " + LocalDateTime.now());
    chattingMessageService.expireSocketSession(event.getSessionId());
    log.info("세션 제거하기 완료 : " + LocalDateTime.now());
  }

  @MessageMapping("/chatting/messages")
  public void message(@Valid ChattingStomp.Request request, SimpMessageHeaderAccessor accessor) {
    ChattingDto response = chattingMessageService.chat(
        accessor.getSessionId(),
        request.roomId(),
        request.content()
    );
    simpMessageSendingOperations.convertAndSend("/sub/chatting/rooms/" + request.roomId(),
        ChattingStomp.Response.from(response));
  }

}
