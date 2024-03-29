package coffeemeet.server.chatting.current.implement;

import static coffeemeet.server.chatting.exception.ChattingErrorCode.CHATTING_ROOM_NOT_FOUND;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.domain.repository.ChattingRoomRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.common.execption.NotFoundException;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingRoomQuery {

  private static final String CHATTING_ROOM_NOT_FOUND_MESSAGE = "(%s)번 채팅방을 찾을 수 없습니다.";

  private final ChattingRoomRepository chattingRoomRepository;

  public ChattingRoom getChattingRoomById(Long roomId) {
    return chattingRoomRepository.findById(roomId)
        .orElseThrow(() -> new InvalidInputException(
            CHATTING_ROOM_NOT_FOUND,
            String.format(CHATTING_ROOM_NOT_FOUND_MESSAGE, roomId)
        ));
  }

  public void verifyChatRoomExistence(Long roomId) {
    if (!chattingRoomRepository.existsById(roomId)) {
      throw new NotFoundException(
          CHATTING_ROOM_NOT_FOUND,
          String.format(CHATTING_ROOM_NOT_FOUND_MESSAGE, roomId)
      );
    }
  }

  public Set<ChattingRoom> getChattingRoomsSetBy(Set<Long> chattingRoomIds) {
    return new HashSet<>(chattingRoomRepository.findByIdIn(chattingRoomIds));
  }

  public boolean existsBy(Long roomId) {
    return chattingRoomRepository.existsById(roomId);
  }

}
