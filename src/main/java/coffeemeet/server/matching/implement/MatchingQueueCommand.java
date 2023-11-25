package coffeemeet.server.matching.implement;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static coffeemeet.server.common.execption.GlobalErrorCode.INTERNAL_SERVER_ERROR;

import coffeemeet.server.common.execption.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingQueueCommand {

    private final RedisTemplate<String, Long> redisTemplate;

    public void enqueueUserByCompanyName(String companyName, Long userId) {
        ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
        Boolean result = zSetOperations.add(companyName, userId, System.currentTimeMillis());

        if (result == null) {
            throw new RedisException(INTERNAL_SERVER_ERROR, "Redis가 Pipeline 상태이거나 Transaction 상태입니다.");
        }
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

    public void deleteUserByUserId(String companyName, Long userId) {
        redisTemplate.opsForZSet().remove(companyName, userId);
    }

}
