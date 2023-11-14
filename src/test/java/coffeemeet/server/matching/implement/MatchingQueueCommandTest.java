package coffeemeet.server.matching.implement;

import static coffeemeet.server.common.execption.GlobalErrorCode.INTERNAL_SERVER_ERROR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.execption.RedisException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

@ExtendWith(MockitoExtension.class)
class MatchingQueueCommandTest {

  @InjectMocks
  private MatchingQueueCommand matchingQueueCommand;

  @Mock
  private RedisTemplate<String, Long> redisTemplate;

  @Mock
  private ZSetOperations<String, Long> zSetOperations;

  @DisplayName("매칭을 요청한 사용자를 해당 회사 이름의 큐에 추가할 수 있다.")
  @Test
  void enqueueUserByCompanyNameTest() {
    // given
    String companyName = "회사명";
    Long userId = 1L;
    Boolean result = true;

    given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
    given(zSetOperations.add(any(), anyLong(), anyDouble())).willReturn(result);

    // when, then
    assertThatCode(() -> matchingQueueCommand.enqueueUserByCompanyName(companyName, userId))
        .doesNotThrowAnyException();
  }

  @DisplayName("매칭을 요청한 사용자를 해당 회사 이름의 큐에 추가할 경우, 추가가 실패한다면 예외가 발생한다.")
  @Test
  void enqueueUserByCompanyNameExceptionTest() {
    // given
    String companyName = "회사명";
    Long userId = 1L;

    given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
    given(zSetOperations.add(any(String.class), anyLong(), anyDouble()))
        .willThrow(
            new RedisException("Redis가 Pipeline 상태이거나 Transaction 상태입니다.", INTERNAL_SERVER_ERROR));

    // when, then
    assertThatThrownBy(
        () -> matchingQueueCommand.enqueueUserByCompanyName(companyName, userId))
        .isInstanceOf(RedisException.class);
  }

}
