package coffeemeet.server.chatting.current.service;

import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {

  private final ChattingRoomCommand chattingRoomCommand;

  public void createChattingRoom() {
    chattingRoomCommand.saveChattingRoom();
  }

}
