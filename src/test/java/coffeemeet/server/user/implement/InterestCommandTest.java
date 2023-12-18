package coffeemeet.server.user.implement;

import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
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
class InterestCommandTest {

  @InjectMocks
  private InterestCommand interestCommand;

  @Mock
  private InterestQuery interestQuery;

  @Mock
  private InterestRepository interestRepository;

  @Test
  @DisplayName("관심사를 모두 저장할 수 있다.")
  void saveAll() {
    // given
    User user = user();
    List<Keyword> keywords = List.of(Keyword.운동, Keyword.맛집);

    // when, then
    assertThatCode(() -> interestCommand.saveAll(keywords, user))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("관심사를 수정할 수 있다.")
  void updateInterests() {
    // given
    User user = user();
    List<Interest> interests = List.of(new Interest(Keyword.게임, user),
        new Interest(Keyword.외국어, user));
    List<Keyword> newKeywords = List.of(Keyword.반려동물, Keyword.여행);

    given(interestQuery.findAllByUserId(anyLong())).willReturn(interests);

    // when, then
    assertThatCode(() -> interestCommand.updateInterests(user, newKeywords))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("관심사를 모두 삭제할 수 있다.")
  void deleteAll() {
    // given
    User user = user();
    List<Interest> interests = List.of(new Interest(Keyword.운동, user),
        new Interest(Keyword.맛집, user));

    // when, then
    assertThatCode(() -> interestCommand.deleteAll(interests))
        .doesNotThrowAnyException();
  }

}
