package coffeemeet.server.chatting.concurrency;

import static coffeemeet.server.user.domain.UserStatus.CHATTING_UNCONNECTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;

import coffeemeet.server.chatting.current.domain.ChattingMessage;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingMessageRepository;
import coffeemeet.server.chatting.current.infrastructure.ChattingRoomRepository;
import coffeemeet.server.chatting.current.service.ChattingMessageService;
import coffeemeet.server.common.fixture.ChattingFixture;
import coffeemeet.server.common.fixture.UserFixture;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
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
import org.springframework.boot.test.mock.mockito.MockBean;
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

  @MockBean
  private FCMNotificationSender fcmNotificationSender;

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
    User user = userRepository.save(UserFixture.user(CHATTING_UNCONNECTED));

    String sessionId = "sessionId";
    chattingMessageService.storeSocketSession(sessionId, String.valueOf(user.getId()));

    int userSize = 4;
    ExecutorService executorService = Executors.newFixedThreadPool(userSize);
    CountDownLatch countDownLatch = new CountDownLatch(userSize);

    willDoNothing().given(fcmNotificationSender)
        .sendNotification(user.getNotificationInfo(), "test");

    // when
    for (int i = 0; i < userSize; i++) {
      executorService.submit(() -> {
        try {
          chattingMessageService.chat(sessionId, room.getId(), "test");
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
    assertThat(messages).hasSize(userSize);
  }

}
