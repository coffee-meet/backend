package coffeemeet.server.matching.implement;

import coffeemeet.server.certification.implement.CertificationQuery;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingQueueCommand {

  private final RedisTemplate<String, Long> redisTemplate;
  private final CertificationQuery certificationQuery;

  public void enqueueUserByCompanyName(String companyName, Long userId) {
    ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
    zSetOperations.add(companyName, userId, System.currentTimeMillis());
  }

  public LocalDateTime getTimeByUserId(String companyName, Long userId) {
    ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
    Double score = zSetOperations.score(companyName, userId);
    if (score == null) {
      return null;
    }
    return Instant.ofEpochMilli(score.longValue())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }

  public void deleteUserByUserId(Long userId) {
    String companyName = certificationQuery.getCompanyNameByUserId(userId);
    redisTemplate.opsForZSet().remove(companyName, userId);
  }

}
