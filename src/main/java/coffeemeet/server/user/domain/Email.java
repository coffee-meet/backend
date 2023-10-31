package coffeemeet.server.user.domain;

import static coffeemeet.server.user.exception.UserErrorCode.INVALID_EMAIL;

import coffeemeet.server.common.execption.MissMatchException;
import coffeemeet.server.common.util.Patterns;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

  private static final String INVALID_EMAIL_MESSAGE = "올바르지 않은 이메일입니다.";

  private String email;

  public Email(@NonNull String email) {
    validateEmail(email);
    this.email = email;
  }

  private void validateEmail(String email) {
    if (!StringUtils.hasText(email) || !Patterns.EMAIL_PATTERN.matcher(email).matches()) {
      throw new MissMatchException(
          INVALID_EMAIL,
          String.format(INVALID_EMAIL_MESSAGE, email)
      );
    }
  }

}
