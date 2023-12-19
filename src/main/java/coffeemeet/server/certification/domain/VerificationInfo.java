package coffeemeet.server.certification.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "email_verification", timeToLive = 300)
public class VerificationInfo {

  @Id
  private Long userId;
  private CompanyEmail companyEmail;
  private String verificationCode;
  private LocalDateTime createdAt;

  public VerificationInfo(@NonNull Long userId, @NonNull CompanyEmail companyEmail,
      @NonNull String verificationCode) {
    this.userId = userId;
    this.companyEmail = companyEmail;
    this.verificationCode = verificationCode;
    this.createdAt = LocalDateTime.now();
  }

}
