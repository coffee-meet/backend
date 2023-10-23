package coffeemeet.server.certification.domain;

import coffeemeet.server.user.domain.CompanyEmail;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "verification_vo", timeToLive = 360)
public class VerificationVo {

  @Id
  private Long userId;
  private CompanyEmail companyEmail;
  private String code;
  private LocalDateTime createdAt;

  public VerificationVo(Long userId, CompanyEmail companyEmail, String code,
      LocalDateTime createdAt) {
    this.userId = userId;
    this.companyEmail = companyEmail;
    this.code = code;
    this.createdAt = createdAt;
  }
}
