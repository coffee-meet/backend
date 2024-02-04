package coffeemeet.server.user.implement;

import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.user.domain.Interest;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.InterestRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InterestQueryTest {

  @InjectMocks
  private InterestQuery interestQuery;

  @Mock
  private InterestRepository interestRepository;

  @DisplayName("유저 아이디로 해당 유저의 키워드를 가져올 수 있다.")
  @Test
  void getKeywordsByUserId() {
    // given
    User user = user();
    List<Interest> interests = List.of(new Interest(Keyword.게임, user),
        new Interest(Keyword.여행, user));

    // when
    given(interestRepository.findAllByUserId(anyLong())).willReturn(interests);
    List<Keyword> result = interestQuery.getKeywordsByUserId(user.getId());

    // then
    assertThat(result).hasSize(2)
        .contains(Keyword.게임, Keyword.여행);
  }

}
