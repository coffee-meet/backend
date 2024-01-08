package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.domain.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingRoomCommand {

  private final ChattingRoomRepository chattingRoomRepository;

  public ChattingRoom createChattingRoom() {
    return chattingRoomRepository.save(new ChattingRoom());
  }

  public void deleteChattingRoom(ChattingRoom chattingRoom) {
    chattingRoomRepository.deleteById(chattingRoom.getId());
  }

}
