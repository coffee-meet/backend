package coffeemeet.server.user.domain;

import coffeemeet.server.common.util.Patterns;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Email {

  private String email;

  public Email(String email) {
    validateEmail(email);
    this.email = email;
  }

  private void validateEmail(String email) {
    if (!Patterns.EMAIL_PATTERN.matcher(email).matches()) {
      throw new IllegalArgumentException("올바르지 않은 이메일입니다.");
    }
  }

}
