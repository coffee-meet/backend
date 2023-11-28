package coffeemeet.server.chatting.current.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingRoomRepository;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChattingRoomQueryTest {

  @InjectMocks
  private ChattingRoomQuery chattingRoomQuery;

  @Mock
  private ChattingRoomRepository chattingRoomRepository;

  @DisplayName("채팅방을 조회할 수 있다.")
  @Test
  void getChattingRoomByIdTest() {
    // given
    ChattingRoom chattingRoom = ChattingFixture.chattingRoom();
    Long chattingRoomId = chattingRoom.getId();
    given(chattingRoomRepository.findById(chattingRoomId)).willReturn(Optional.of(chattingRoom));

    // when
    ChattingRoom foundChattingRoom = chattingRoomQuery.getChattingRoomById(chattingRoomId);

    // then
    assertThat(foundChattingRoom).isEqualTo(chattingRoom);
  }

  @DisplayName("채팅방이 존재하지 않으면 예외가 발생할 수 있다.")
  @Test
  void verifyChatRoomExistenceTest() {
    // given
    Long roomId = 1L;
    given(chattingRoomRepository.existsById(roomId)).willReturn(false);

    // when, then
    assertThatThrownBy(() -> chattingRoomQuery.verifyChatRoomExistence(roomId))
        .isInstanceOf(NotFoundException.class);
  }

  @DisplayName("채팅방 아이디 셋으로 채팅방을 셋을 조회할 수 있다.")
  @Test
  void getChattingRoomsSetByIdSetTest() {
    // given
    int size = 4;
    Set<ChattingRoom> chattingRooms = ChattingFixture.chattingRoom(size);
    Set<Long> chattingRoomIdsSet = chattingRooms.stream().map(chattingRoom -> chattingRoom.getId())
        .collect(Collectors.toSet());
    given(chattingRoomRepository.findByIdIn(anySet())).willReturn(chattingRooms);

    // when
    Set<ChattingRoom> response = chattingRoomQuery.getChattingRoomsSetBy(chattingRoomIdsSet);

    // then
    assertThat(response).isEqualTo(chattingRooms);
  }

  @DisplayName("채팅방 존재를 확인할 수 있다.")
  @Test
  void existsByRoomIdTest() {
    // given
    Long roomId = 1L;
    given(chattingRoomRepository.existsById(any())).willReturn(true);

    // when
    boolean response = chattingRoomQuery.existsBy(roomId);

    // then
    assertThat(response).isTrue();
  }

}
