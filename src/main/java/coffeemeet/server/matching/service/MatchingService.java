package coffeemeet.server.matching.service;

import static coffeemeet.server.matching.exception.MatchingErrorCode.INVALID_USER_STATUS;
import static coffeemeet.server.user.domain.UserStatus.MATCHING;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.implement.ChattingRoomCommand;
import coffeemeet.server.common.execption.BadRequestException;
import coffeemeet.server.common.infrastructure.FCMNotificationSender;
import coffeemeet.server.matching.implement.MatchingQueueCommand;
import coffeemeet.server.matching.implement.MatchingQueueQuery;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {

  private static final String NOT_CERTIFICATED_USER_MESSAGE = "사용자(%s) 인증이 완료되지 않았습니다.";
  private static final long FIXED_MATCH_GROUP_SIZE = 4;

  private final FCMNotificationSender notificationSender;
  private final CertificationQuery certificationQuery;
  private final UserQuery userQuery;
  private final UserCommand userCommand;
  private final ChattingRoomCommand chattingRoomCommand;
  private final MatchingQueueQuery matchingQueueQuery;
  private final MatchingQueueCommand matchingQueueCommand;

  public void startMatching(Long userId) {
    Certification certification = certificationQuery.getCertificationByUserId(userId);
//    if (!certification.isCertificated()) {
//      throw new ForbiddenException(NOT_CERTIFICATED_USER,
//          String.format(NOT_CERTIFICATED_USER_MESSAGE, userId));
//    }

    String companyName = certification.getCompanyName();
    matchingQueueCommand.enqueueUserByCompanyName(companyName, userId);
    userCommand.setToMatching(userId);

    long matchingQueueSizeByCompany = matchingQueueQuery.sizeByCompany(companyName);
    if (matchingQueueSizeByCompany >= FIXED_MATCH_GROUP_SIZE) {
      processMatching(companyName);
    }
  }

  private void processMatching(String companyName) {
    Set<Long> userIds = matchingQueueQuery.dequeueMatchingGroupSize(companyName,
        FIXED_MATCH_GROUP_SIZE);
    ChattingRoom chattingRoom = chattingRoomCommand.createChattingRoom();
    userCommand.assignUsersToChattingRoom(userIds, chattingRoom);

    Set<NotificationInfo> notificationInfos = userQuery.getNotificationInfosByIdSet(userIds);
    notificationSender.sendMultiNotificationsWithData(notificationInfos, "두근두근 커피밋 채팅을 시작하세요!",
        "chattingRoomId", String.valueOf(chattingRoom.getId()));
  }

  public void cancelMatching(Long userId) {
    User user = userQuery.getUserById(userId);
    if (user.getUserStatus() != MATCHING) {
      throw new BadRequestException(INVALID_USER_STATUS,
          String.format("유저 상태가 %s이 아닙니다.", MATCHING));
    }
    String companyName = certificationQuery.getCompanyNameByUserId(userId);
    matchingQueueCommand.deleteUserByUserId(companyName, userId);
    userCommand.setToIdle(userId);
  }

}
