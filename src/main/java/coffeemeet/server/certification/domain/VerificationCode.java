package coffeemeet.server.certification.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "verification_code", timeToLive = 360)
public class VerificationCode {

  @Id
  private Long userId;
  private String code;
  private LocalDateTime createdAt;

  public VerificationCode(Long userId, String code, LocalDateTime createdAt) {
    this.userId = userId;
    this.code = code;
    this.createdAt = createdAt;
  }

}
