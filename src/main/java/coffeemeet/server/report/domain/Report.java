package coffeemeet.server.report.domain;

import static coffeemeet.server.report.exception.ReportErrorCode.INVALID_REASON_DETAIL;

import coffeemeet.server.common.domain.BaseEntity;
import coffeemeet.server.common.execption.DataLengthExceededException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private ReportReason reason;

  @Column(nullable = false, length = REASON_MAX_LENGTH)
  private String reasonDetail;

  @Builder
  private Report(
      @NonNull Long reporterId,
      @NonNull Long chattingRoomId,
      @NonNull Long targetId,
      @NonNull String reason,
      @NonNull String reasonDetail
  ) {
    validateReasonDetails(reasonDetail);
    this.reporterId = reporterId;
    this.chattingRoomId = chattingRoomId;
    this.targetId = targetId;
    this.reason = ReportReason.getReason(reason);
    this.reasonDetail = reasonDetail;
  }

  private void validateReasonDetails(String reasonDetails) {
    if (!StringUtils.hasText(reasonDetails) || reasonDetails.length() > REASON_MAX_LENGTH) {
      throw new DataLengthExceededException(
          INVALID_REASON_DETAIL,
          String.format(INVALID_REASON_DETAIL_MESSAGE, reasonDetails)
      );
    }
  }

}
