package coffeemeet.server.user.implement;

import static coffeemeet.server.user.exception.UserErrorCode.ALREADY_EXIST_NICKNAME;

import coffeemeet.server.common.execption.DuplicatedDataException;
import coffeemeet.server.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DuplicatedNicknameValidator {

  private static final String ALREADY_EXIST_NICKNAME_MESSAGE = "해당 닉네임(%s)은 이미 존재하는 닉네임입니다.";

  private final UserRepository userRepository;

  public void validate(String nickname) {
    if (userRepository.existsUserByProfile_Nickname(nickname)) {
      throw new DuplicatedDataException(
          ALREADY_EXIST_NICKNAME,
          String.format(ALREADY_EXIST_NICKNAME_MESSAGE, nickname)
      );
    }
  }

  public void validateExceptUserId(String nickname, Long userId) {
    if (userRepository.existsByNicknameAndNotUserId(nickname, userId)) {
      throw new DuplicatedDataException(
          ALREADY_EXIST_NICKNAME,
          String.format(ALREADY_EXIST_NICKNAME_MESSAGE, nickname)
      );
    }
  }


}
