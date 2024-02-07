package coffeemeet.server.user.implement;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.user.infrastructure.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DuplicatedNicknameValidatorTest {

  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private DuplicatedNicknameValidator duplicatedNicknameValidator;

  @Test
  @DisplayName("닉네임 중복 체크할 수 있다.")
  void validateTest() {
    // given
    String nickname = "nickname";

    given(userRepository.existsUserByProfile_Nickname(any())).willReturn(Boolean.FALSE);

    // when, then
    assertThatCode(() -> duplicatedNicknameValidator.validate(nickname))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("특정 사용자를 제외한 닉네임 중복 체크할 수 있다.")
  void validateExceptUserIdTest() {
    // given
    String nickname = "nickname";
    Long userId = 1L;

    given(userRepository.existsByNicknameAndNotUserId(nickname, userId)).willReturn(Boolean.FALSE);

    // when, then
    assertThatCode(() -> duplicatedNicknameValidator.validateExceptUserId(nickname, userId))
        .doesNotThrowAnyException();
  }

}
