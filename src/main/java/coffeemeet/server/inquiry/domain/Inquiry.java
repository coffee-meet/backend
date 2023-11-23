package coffeemeet.server.inquiry.domain;

import coffeemeet.server.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@Table(name = "inquiries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseEntity {

  private static final int TITLE_MAX_LENGTH = 20;
  private static final int CONTENT_MAX_LENGTH = 200;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long inquirerId;

  @Column(nullable = false, length = TITLE_MAX_LENGTH)
  private String title;

  @Column(nullable = false, length = CONTENT_MAX_LENGTH)
  private String content;

  @Column(nullable = false)
  private boolean isCheck;

  public Inquiry(Long inquirerId, String title, String content) {
    validateInquirer(inquirerId);
    validateTitle(title);
    validateContent(content);
    this.inquirerId = inquirerId;
    this.title = title;
    this.content = content;
  }

  public void checkInquiry() {
    this.isCheck = true;
  }

  private void validateTitle(String title) {
    if (!StringUtils.hasText(title) || title.length() > TITLE_MAX_LENGTH) {
      throw new IllegalArgumentException("올바르지 않은 제목입니다.");
    }
  }

  private void validateContent(String content) {
    if (!StringUtils.hasText(content) || content.length() > CONTENT_MAX_LENGTH) {
      throw new IllegalArgumentException("올바르지 않은 내용입니다.");
    }
  }

  private void validateInquirer(Long inquirerId) {
    if (inquirerId == null) {
      throw new IllegalArgumentException("올바르지 않은 사용자 아이디입니다.");
    }
  }

}
