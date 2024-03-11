package coffeemeet.server.matching.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.certification.implement.CertificationQuery;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
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

  @Mock
  private CertificationQuery certificationQuery;

  @BeforeEach
  void setUp() {
    given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
  }

  @DisplayName("매칭을 요청한 사용자를 해당 회사 이름의 큐에 추가할 수 있다.")
  @Test
  void enqueueUserByCompanyNameTest() {
    // given
    String companyName = "회사명";
    Long userId = 1L;
    Boolean result = true;

    given(zSetOperations.add(any(), anyLong(), anyDouble())).willReturn(result);

    // when, then
    assertThatCode(() -> matchingQueueCommand.enqueueUserByCompanyName(companyName, userId))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("매칭큐에 들어있는 사용자를 지울 수 있다.")
  void deleteUserByUserIdTest() {
    // given
    String companyName = "회사명";
    Long userId = 1L;
    given(certificationQuery.getCompanyNameByUserId(userId)).willReturn(companyName);

    // when
    matchingQueueCommand.deleteUserByUserId(userId);

    // then
    then(zSetOperations).should(only()).remove(companyName, userId);
  }

  @Test
  @DisplayName("유저의 매칭 시작 시간을 가져올 수 있다.")
  void getTimeByUserIdTest() {
    // given
    Double score = 1.1;
    given(zSetOperations.score(any(), anyLong())).willReturn(score);

    // when
    LocalDateTime matchingStartedAt = matchingQueueCommand.getTimeByUserId("회사명", 1L);

    // then
    assertThat(matchingStartedAt).isNotNull();
  }

}
