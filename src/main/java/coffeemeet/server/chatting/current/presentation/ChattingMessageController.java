package coffeemeet.server.chatting.current.presentation;

import coffeemeet.server.chatting.current.presentation.dto.ChatStomp;
import coffeemeet.server.chatting.current.service.ChattingMessageService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@RequiredArgsConstructor
public class ChattingMessageController {

  private final SimpMessageSendingOperations simpMessageSendingOperations;
  private final ChattingMessageService chattingMessageService;
  private final Map<String, Long> sessions = new HashMap<>();

  @EventListener(SessionConnectEvent.class)
  public void onConnect(SessionConnectEvent event) {
    String sessionId = String.valueOf(event.getMessage().getHeaders().get("simpSessionId"));
    String userId = String.valueOf(event.getMessage().getHeaders().get("nativeHeaders"))
        .split("userId=\\[")[1].split("]")[0];
    sessions.put(sessionId, Long.valueOf(userId));
  }

  @EventListener(SessionDisconnectEvent.class)
  public void onDisconnect(SessionDisconnectEvent event) {
    sessions.remove(event.getSessionId());
  }

  @MessageMapping("/chatting/messages")
  public void message(@Valid ChatStomp.Request request, SimpMessageHeaderAccessor accessor) {
    Long userId = sessions.get(accessor.getSessionId());
    chattingMessageService.createChattingMessage(request.roomId(), request.content(), userId);
    simpMessageSendingOperations.convertAndSend("/sub/chatting/room/" + request.roomId(),
        request.content());
  }

}