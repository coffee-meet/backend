package coffeemeet.server.user.infrastructure;

import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.common.fixture.entity.UserFixture;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.User;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryTest extends RepositoryTestConfig {

  @Autowired
  private UserRepository userRepository;

  private User user;

  @BeforeEach
  void setUp() {
    user = UserFixture.user();
    userRepository.save(user);
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("로그인 정보로 유저의 존재 여부를 판단할 수 있다.")
  void existsUserByOauthInfoTest() {
    // given
    OAuthInfo oauthInfo = user.getOauthInfo();

    // when, then
    assertThat(userRepository.existsUserByOauthInfo(oauthInfo)).isEqualTo(Boolean.TRUE);
  }

  @Test
  @DisplayName("아이디들로 해당 유저 Set을 찾을 수 있다.")
  void findByIdInTest() {
    // given
    User user1 = user();
    userRepository.save(user1);

    Long id = user.getId();
    Long id1 = user1.getId();

    Set<User> users = new HashSet<>(Set.of(user, user1));
    Set<Long> ids = new HashSet<>(Set.of(id, id1));

    // when, then
    assertThat(userRepository.findByIdIn(ids)).isEqualTo(users);
  }

  @Test
  @DisplayName("로그인 정보로 해당 유저를 찾을 수 있다.")
  void findByOauthInfoTest() {
    // given
    OAuthInfo oauthInfo = user.getOauthInfo();

    // when, then
    assertThat(userRepository.findByOauthInfo(oauthInfo)).isEqualTo(Optional.of(user));
  }

  @Test
  @DisplayName("닉네임으로 유저의 존재 여부를 판단할 수 있다.")
  void existsUserByProfile_NicknameTest() {
    // given
    String nickname = user.getProfile().getNickname();

    // when, then
    assertThat(userRepository.existsUserByProfile_Nickname(nickname)).isEqualTo(Boolean.TRUE);
  }

}
