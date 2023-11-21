package coffeemeet.server.user.domain;

import static coffeemeet.server.user.exception.UserErrorCode.INVALID_NICKNAME;

import coffeemeet.server.common.execption.DataLengthExceededException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

  private static final int NICKNAME_MAX_LENGTH = 20;
  private static final String INVALID_NICKNAME_MESSAGE = "올바르지 않은 닉네임(%s)입니다.";

  @Column(length = NICKNAME_MAX_LENGTH)
  private String nickname;

  public Profile(
      @NonNull String nickname
  ) {
    validateNickname(nickname);
    this.nickname = nickname;
  }

  public void updateNickname(String newNickname) {
    validateNickname(nickname);
    this.nickname = newNickname;
  }

  private void validateNickname(String nickname) {
    if (!StringUtils.hasText(nickname) || nickname.length() > NICKNAME_MAX_LENGTH) {
      throw new DataLengthExceededException(
          INVALID_NICKNAME,
          String.format(INVALID_NICKNAME_MESSAGE, nickname)
      );
    }
  }

}
