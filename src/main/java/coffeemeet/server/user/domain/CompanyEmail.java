package coffeemeet.server.user.domain;

import coffeemeet.server.common.util.Patterns;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class CompanyEmail {

  private String companyEmail;

  public CompanyEmail(String companyEmail) {
    validateCompanyEmail(companyEmail);
    this.companyEmail = companyEmail;
  }

  private void validateCompanyEmail(String companyEmail) {
    if (!Patterns.EMAIL_PATTERN.matcher(companyEmail).matches()) {
      throw new IllegalArgumentException("올바르지 않은 이메일입니다.");
    }
  }

}
