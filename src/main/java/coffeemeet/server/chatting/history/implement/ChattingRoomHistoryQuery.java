package coffeemeet.server.chatting.history.implement;

import static coffeemeet.server.chatting.exception.ChattingErrorCode.CHATTING_ROOM_HISTORY_NOT_FOUND;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.infrastructure.ChattingRoomHistoryRepository;
import coffeemeet.server.common.execption.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingRoomHistoryQuery {

  private static final String CHATTING_ROOM_HISTORY_NOT_FOUND_MESSAGE = "채팅방 (%s) 내역을 조회할 수 없습니다.";

  private final ChattingRoomHistoryRepository chattingRoomHistoryRepository;

  public ChattingRoomHistory getChattingRoomHistoryBy(Long roomHistoryId) {
    return chattingRoomHistoryRepository.findById(roomHistoryId)
        .orElseThrow(() -> new InvalidInputException(
            CHATTING_ROOM_HISTORY_NOT_FOUND,
            String.format(CHATTING_ROOM_HISTORY_NOT_FOUND_MESSAGE, roomHistoryId)
        ));
  }

}
