package coffeemeet.server.matching.implement;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.common.domain.UsersNotificationEvent;
import coffeemeet.server.user.implement.UserCommand;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MatchingConditionChecker {

  private static final long FIXED_MATCH_GROUP_SIZE = 4;

  private final UserCommand userCommand;
  private final ChattingRoomCommand chattingRoomCommand;
  private final MatchingQueueQuery matchingQueueQuery;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Transactional
  public void check(String companyName) {
    long matchingQueueSizeByCompany = matchingQueueQuery.sizeByCompany(companyName);
    if (matchingQueueSizeByCompany >= FIXED_MATCH_GROUP_SIZE) {
      Set<Long> userIds = matchingQueueQuery.dequeueMatchingGroupSize(companyName,
          FIXED_MATCH_GROUP_SIZE);
      ChattingRoom chattingRoom = chattingRoomCommand.createChattingRoom();
      userCommand.assignUsersToChattingRoom(userIds, chattingRoom);
      applicationEventPublisher.publishEvent(
          new UsersNotificationEvent(userIds, "두근두근 커피밋 채팅을 시작하세요!"));
    }
  }

}
