package coffeemeet.server.report.domain;

import coffeemeet.server.common.domain.BaseEntity;
import jakarta.persistence.Column;
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

  private static final int TITLE_MAX_LENGTH = 20;
  private static final int REASON_MAX_LENGTH = 200;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long reporterId;

  @Column(nullable = false)
  private Long targetId;

  @Column(nullable = false, length = TITLE_MAX_LENGTH)
  private String title;

  @Column(nullable = false, length = REASON_MAX_LENGTH)
  private String reason;

  @Column(nullable = false)
  private boolean isProcessed;

  @Builder
  private Report(
      @NonNull Long reporterId,
      @NonNull Long targetId,
      @NonNull String title,
      @NonNull String reason
  ) {
    validateTitle(title);
    validateReason(reason);
    this.reporterId = reporterId;
    this.targetId = targetId;
    this.title = title;
    this.reason = reason;
    isProcessed = false;
  }

  private void validateTitle(String title) {
    if (!StringUtils.hasText(title) || title.length() > TITLE_MAX_LENGTH) {
      throw new IllegalArgumentException("올바르지 않은 제목입니다.");
    }
  }

  private void validateReason(String reason) {
    if (!StringUtils.hasText(reason) || reason.length() > REASON_MAX_LENGTH) {
      throw new IllegalArgumentException("올바르지 않은 사유입니다.");
    }
  }
  public void processed() {
    isProcessed = true;
  }

}
