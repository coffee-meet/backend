package coffeemeet.server.user.domain;

import coffeemeet.server.common.util.Patterns;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

  private String email;

  public Email(@NonNull String email) {
    validateEmail(email);
    this.email = email;
  }

  private void validateEmail(String email) {
    if (!Patterns.EMAIL_PATTERN.matcher(email).matches()) {
      throw new IllegalArgumentException("올바르지 않은 이메일입니다.");
    }
  }

}
