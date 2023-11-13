package coffeemeet.server.matching.implement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

@ExtendWith(MockitoExtension.class)
class MatchingQueueQueryTest {

  @InjectMocks
  private MatchingQueueQuery matchingQueueQuery;

  @Mock
  private RedisTemplate<String, Long> redisTemplate;

  @Mock
  private ZSetOperations<String, Long> zSetOperations;

  @DisplayName("매칭큐의 사이즈를 조회할 수 있다.")
  @Test
  void sizeByCompany() {
    // given
    Long size = 4L;
    String companyName = "회사명";

    given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
    given(zSetOperations.size(any())).willReturn(size);

    // when
    long expectedSize = matchingQueueQuery.sizeByCompany(companyName);

    // then
    Assertions.assertThat(size).isEqualTo(expectedSize);
  }

  @DisplayName("지정 사이즈만큼 매칭큐의 사용자를 제거한다.")
  @Test
  void dequeueMatchingGroupSize() {
    // given
    String companyName = "회사명";
    long matchingGroupSize = 4;
    Set<Long> expectedUserIds = new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L));

    given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
    given(zSetOperations.range(companyName, 0, matchingGroupSize - 1)).willReturn(expectedUserIds);
    given(zSetOperations.removeRange(companyName, 0, matchingGroupSize - 1)).willReturn(4L);

    // when
    Set<Long> userIds = matchingQueueQuery.dequeueMatchingGroupSize(companyName, matchingGroupSize);

    // then
    Assertions.assertThat(userIds).isEqualTo(expectedUserIds);
  }

}
