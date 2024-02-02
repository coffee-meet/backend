package coffeemeet.server.user.implement;

import static coffeemeet.server.common.fixture.UserFixture.user;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
  private UserQuery userQuery;

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
  void updateInterestsTest() {
    // given
    User user = user();
    Long userId = user.getId();
    List<Keyword> newKeywords = List.of(Keyword.반려동물, Keyword.여행);

    given(userQuery.getUserById(userId)).willReturn(user);

    // when
    interestCommand.updateInterests(user.getId(), newKeywords);

    // then
    then(interestRepository).should(times(1)).deleteAllByUserId(userId);
    then(interestRepository).should(times(1)).saveAll(any());
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
