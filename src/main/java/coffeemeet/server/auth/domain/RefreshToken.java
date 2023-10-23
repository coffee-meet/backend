package coffeemeet.server.auth.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refresh", timeToLive = 1209600)
public class RefreshToken {

  @Id
  private Long userId;

  private String value;

  @Builder
  private RefreshToken(Long userId, String value) {
    this.userId = userId;
    this.value = value;
  }

}
