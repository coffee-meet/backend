package coffeemeet.server.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingMessageRepository;
import coffeemeet.server.chatting.current.infrastructure.ChattingRoomRepository;
import coffeemeet.server.chatting.current.service.ChattingMessageService;
import coffeemeet.server.common.fixture.entity.ChattingFixture;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ChattingMessageServiceConcurrencyTest {

  @Autowired
  private ChattingMessageService chattingMessageService;

  @Autowired
  private ChattingRoomRepository chattingRoomRepository;

  @Autowired
  private ChattingMessageRepository chattingMessageRepository;

  @Autowired
  private UserRepository userRepository;

  @AfterEach
  void tearDown() {
    chattingMessageRepository.deleteAll();
    chattingRoomRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  @DisplayName("채팅 메세지 저장 동시성 테스트")
  void chattingConcurrencyTest() throws InterruptedException {
    // given
    ChattingRoom room = chattingRoomRepository.save(ChattingFixture.chattingRoom());
    User user = userRepository.save(UserFixture.user());

    int count = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(count);
    CountDownLatch countDownLatch = new CountDownLatch(count);

    // when
    for (int i = 0; i < count; i++) {
      executorService.submit(() -> {
        try {
          chattingMessageService.chatting(room.getId(), "test", user.getId());
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          countDownLatch.countDown();
        }
      });
    }
    countDownLatch.await();

    // then
    List<ChattingMessage> messages = chattingMessageRepository.findAll();
    assertThat(messages).hasSize(count);
  }

}
