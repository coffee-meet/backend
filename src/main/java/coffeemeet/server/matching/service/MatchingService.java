package coffeemeet.server.matching.service;

import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.common.implement.FCMNotificationSender;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.matching.implement.MatchingQueueQuery;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

  private static final long FIXED_MATCH_GROUP_SIZE = 4;

  private final FCMNotificationSender notificationSender;
  private final CertificationQuery certificationQuery;
  private final UserQuery userQuery;
  private final UserCommand userCommand;
  private final ChattingRoomCommand chattingRoomCommand;
  private final MatchingQueueQuery matchingQueueQuery;
  private final MatchingQueueCommand matchingQueueCommand;

  public void start(Long userId) {
    String companyName = certificationQuery.getCompanyNameByUserId(userId);
    matchingQueueCommand.enqueueUserByCompanyName(companyName, userId);

    long matchingQueueSizeByCompany = matchingQueueQuery.sizeByCompany(companyName);
    if (matchingQueueSizeByCompany >= FIXED_MATCH_GROUP_SIZE) {
      match(companyName);
    }
  }

  private void match(String companyName) {
    Set<Long> userIds = matchingQueueQuery.dequeueMatchingGroupSize(companyName,
        FIXED_MATCH_GROUP_SIZE);
    ChattingRoom chattingRoom = chattingRoomCommand.createChattingRoom();

    userCommand.assignUsersToChattingRoom(userIds, chattingRoom);

    Set<NotificationInfo> notificationInfos = userQuery.getNotificationInfosByIdSet(userIds);
    notificationSender.sendMultiNotificationsWithData(notificationInfos, "두근두근 커피밋 채팅을 시작하세요!",
        "chattingRoomId", String.valueOf(chattingRoom.getId()));
  }

}