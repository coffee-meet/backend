package coffeemeet.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationInfo {

  @Column(name = "firebase_notification_token")
  private String token;

  @Column(name = "created_firebase_notification_token_at")
  private LocalDateTime createdTokenAt;

  public NotificationInfo(String token, LocalDateTime createdTokenAt) {
    this.token = token;
    this.createdTokenAt = createdTokenAt;
  }

}
