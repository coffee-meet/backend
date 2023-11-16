package coffeemeet.server.matching.implement;

import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingQueueQuery {

  private static final long REDIS_IN_PIPELINE_OR_TRANSACTION = 0;

  private final RedisTemplate<String, Long> redisTemplate;

  public long sizeByCompany(String companyName) {
    Long size = redisTemplate.opsForZSet().size(companyName);
    return size == null ? REDIS_IN_PIPELINE_OR_TRANSACTION : size;
  }

  public Set<Long> dequeueMatchingGroupSize(String companyName, long matchingGroupSize) {
    ZSetOperations<String, Long> operations = redisTemplate.opsForZSet();
    Set<Long> userIds = operations.range(companyName, 0, matchingGroupSize - 1);
    operations.removeRange(companyName, 0, matchingGroupSize - 1);
    return userIds == null ? Collections.emptySet() : userIds;
  }

}
