package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.presentation.dto.ChatStomp;
import org.instancio.Instancio;

public class ChattingFixture {

  public static ChattingMessage chattingMessage() {
    return Instancio.of(ChattingMessage.class)
        .create();
  }

  public static ChattingMessage chattingMessage(String content) {
    return Instancio.of(ChattingMessage.class)
        .set(field(ChattingMessage::getMessage), content)
        .create();
  }

  public static ChattingRoom chattingRoom() {
    return Instancio.of(ChattingRoom.class)
        .create();
  }

  public static ChatStomp.Request chatStompRequest() {
    return Instancio.of(ChatStomp.Request.class)
        .create();
  }

}
