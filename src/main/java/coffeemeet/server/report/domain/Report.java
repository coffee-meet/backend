package coffeemeet.server.report.domain;

import static coffeemeet.server.report.exception.ReportErrorCode.INVALID_CHATTING_ROOM;
import static coffeemeet.server.report.exception.ReportErrorCode.INVALID_REASON;
import static coffeemeet.server.report.exception.ReportErrorCode.INVALID_REASON_DETAIL;
import static coffeemeet.server.report.exception.ReportErrorCode.INVALID_REPORTER;
import static coffeemeet.server.report.exception.ReportErrorCode.INVALID_TARGET_USER;

import coffeemeet.server.common.domain.BaseEntity;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.user.domain.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.StringUtils;

@Entity
@Getter
@Table(name = "reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

  private static final String INVALID_REPORTER_MESSAGE = "해당 신고자 아이디(%s)는 유효하지 않습니다.";
  private static final String INVALID_CHATTING_ROOM_MESSAGE = "해당 채팅방 아이디(%s)는 유효하지 않습니다.";
  private static final String INVALID_TARGET_USER_MESSAGE = "해당 신고 대상 아이디(%s)는 유효하지 않습니다.";
  private static final String INVALID_REPORT_REASON_MESSAGE = "해당 신고 사유(%s)는 존재하지 않습니다.";
  private static final String INVALID_REASON_MESSAGE = "해당 신고 사유(%s)는 유효하지 않습니다.";
  private static final String INVALID_REASON_DETAIL_MESSAGE = "해당 신고 상세 사유(%s)는 유효하지 않습니다.";

  private static final int REASON_MAX_LENGTH = 200;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long reporterId;

  @Column(nullable = false)
  private Long chattingRoomId;

  @Column(nullable = false)
  private Long targetId;

  @Embedded
  @Column(nullable = false)
  private Email reporterEmail;

  @Column(nullable = false)
  private String reason;

  @Column(nullable = false, length = REASON_MAX_LENGTH)
  private String reasonDetail;

  @Column(nullable = false)
  private boolean isProcessed;

  @Builder
  private Report(
      @NonNull Long reporterId,
      @NonNull Long chattingRoomId,
      @NonNull Long targetId,
      @NonNull Email reporterEmail,
      @NonNull String reason,
      @NonNull String reasonDetail
  ) {
    validateReporter(reporterId);
    validateChattingRoom(chattingRoomId);
    validateTarget(targetId);
    validateReason(reason);
    validateReasonDetails(reasonDetail);
    this.reporterId = reporterId;
    this.chattingRoomId = chattingRoomId;
    this.targetId = targetId;
    this.reporterEmail = reporterEmail;
    this.reason = reason;
    this.reasonDetail = reasonDetail;
    this.isProcessed = false;
  }

  private void validateReporter(Long reporterId) {
    if (reporterId == null) {
      throw new InvalidInputException(
          INVALID_REPORTER,
          INVALID_REPORTER_MESSAGE
      );
    }
  }

  private void validateChattingRoom(Long chattingRoomId) {
    if (chattingRoomId == null) {
      throw new InvalidInputException(
          INVALID_CHATTING_ROOM,
          INVALID_CHATTING_ROOM_MESSAGE
      );
    }
  }

  private void validateTarget(Long targetId) {
    if (targetId == null) {
      throw new InvalidInputException(
          INVALID_TARGET_USER,
          INVALID_TARGET_USER_MESSAGE
      );
    }
  }

  private void validateReason(String reason) {
    if (!ReportReason.checkReason(reason)) {
      throw new InvalidInputException(
          INVALID_REASON,
          String.format(INVALID_REPORT_REASON_MESSAGE, reason)
      );
    }
  }

  private void validateReasonDetails(String reasonDetails) {
    if (!StringUtils.hasText(reasonDetails) || reasonDetails.length() > REASON_MAX_LENGTH) {
      throw new InvalidInputException(
          INVALID_REASON_DETAIL,
          String.format(INVALID_REASON_DETAIL_MESSAGE, reasonDetails)
      );
    }
  }

}
