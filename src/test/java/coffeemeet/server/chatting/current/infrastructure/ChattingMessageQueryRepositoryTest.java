package coffeemeet.server.chatting.current.infrastructure;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class ChattingMessageQueryRepositoryTest extends RepositoryTestConfig {

  @Autowired
  private ChattingMessageRepository chattingMessageRepository;

  @Autowired
  private ChattingMessageQueryRepository chattingMessageQueryRepository;

  @Autowired
  private ChattingRoomRepository chattingRoomRepository;

  @Autowired
  private UserRepository userRepository;

  @AfterEach
  void tearDown() {
    chattingMessageRepository.deleteAll();
    chattingRoomRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("firstMessageId 보다 1 작은 메세지부터 페이지 사이즈만큼 조회한다.")
  @ParameterizedTest
  @CsvSource(value = {"51, 50"})
  void findChattingMessagesTest(Long firstMessageId, int pageSize) {
    // given
    int fixtureSize = 110;

    ChattingRoom room = chattingRoomRepository.save(ChattingFixture.chattingRoom());
    User user = userRepository.save(UserFixture.user());
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(room, user,
        fixtureSize);
    chattingMessageRepository.saveAll(chattingMessages);

    // when
    List<ChattingMessage> responses = chattingMessageQueryRepository.findChattingMessages(room,
        firstMessageId, pageSize);

    // then
    int lastIndex = responses.size() - 1;
    assertAll(
        () -> assertThat(responses).hasSize(pageSize),
        () -> assertThat(responses.get(lastIndex).getId()).isEqualTo(firstMessageId - 1)
    );
  }

  @DisplayName("채팅방에 있는 메세지를 전체 조회할 수 있다.")
  @Test
  void findAllChattingMessagesByChattingRoomTest() {
    // given
    int fixtureSize = 110;

    ChattingRoom room = chattingRoomRepository.save(ChattingFixture.chattingRoom());
    User user = userRepository.save(UserFixture.user());
    List<ChattingMessage> chattingMessages = ChattingFixture.chattingMessages(room, user,
        fixtureSize);
    chattingMessageRepository.saveAll(chattingMessages);

    // when
    List<ChattingMessage> messageList = chattingMessageQueryRepository.findAllChattingMessagesByChattingRoom(
        room);

    // then
    assertThat(messageList).hasSize(fixtureSize);
  }

}
