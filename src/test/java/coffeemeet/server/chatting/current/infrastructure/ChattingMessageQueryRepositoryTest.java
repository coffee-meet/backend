package coffeemeet.server.chatting.current.infrastructure;

import static coffeemeet.server.common.fixture.ChattingFixture.chattingMessages;
import static coffeemeet.server.common.fixture.ChattingFixture.chattingRoom;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.domain.repository.ChattingMessageRepository;
import coffeemeet.server.chatting.current.domain.repository.ChattingRoomRepository;
import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class ChattingMessageQueryRepositoryTest extends RepositoryTestConfig {

  @Autowired
  private EntityManager entityManager;
  @Autowired
  private ChattingMessageRepository chattingMessageRepository;
  @Autowired
  private ChattingMessageQueryRepository chattingMessageQueryRepository;
  @Autowired
  private ChattingRoomRepository chattingRoomRepository;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    this.entityManager
        .createNativeQuery("ALTER TABLE chatting_messages ALTER COLUMN id RESTART WITH 1")
        .executeUpdate();
  }

  @AfterEach
  void tearDown() {
    chattingMessageRepository.deleteAll();
    chattingRoomRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("cursor id 값보다 작은 메세지를 페이지 사이즈만큼 조회한다.")
  @ParameterizedTest
  @CsvSource(value = {"51, 50"})
  void findChattingMessagesLessThanMessageId(Long cursorId, int pageSize) {
    // given
    int fixtureSize = 110;

    ChattingRoom room = chattingRoomRepository.save(chattingRoom());
    User user = userRepository.save(user());
    List<ChattingMessage> chattingMessages = chattingMessages(room, user,
        fixtureSize);
    List<ChattingMessage> messages = chattingMessageRepository.saveAll(chattingMessages);
    for (ChattingMessage message : messages) {
      System.out.println("message.getId() = " + message.getId());
    }

    // when
    List<ChattingMessage> responses = chattingMessageQueryRepository.findChattingMessagesLessThanCursorId(
        room,
        cursorId, pageSize);

    // then
    assertAll(
        () -> assertThat(responses).hasSize(pageSize),
        () -> assertThat(responses).allMatch(message -> message.getId() < cursorId)
    );
  }

  @Test
  @DisplayName("cursor id 값보다 작거나 같은 메세지를 페이지 사이즈만큼 조회한다.")
  void findChattingMessagesLessThanOrEqualToMessageId() {
    // given
    int fixtureSize = 110;
    ChattingRoom room = chattingRoomRepository.save(chattingRoom());
    User user = userRepository.save(user());
    List<ChattingMessage> chattingMessages = chattingMessages(room, user, fixtureSize);
    chattingMessageRepository.saveAll(chattingMessages);

    Long cursorId = 50L;
    int pageSize = 50;

    // when
    List<ChattingMessage> responses = chattingMessageQueryRepository.findChattingMessagesLessThanOrEqualToCursorId(
        room, cursorId, pageSize);

    // then
    assertAll(
        () -> assertThat(responses).hasSize(pageSize),
        () -> assertThat(responses).allMatch(message -> message.getId() <= cursorId)
    );
  }

}
