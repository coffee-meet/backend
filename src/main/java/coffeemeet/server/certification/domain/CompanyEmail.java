package coffeemeet.server.certification.domain;

import coffeemeet.server.common.util.Patterns;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyEmail {

  @Column(name = "company_email")
  private String value;

  public CompanyEmail(String value) {
    validateCompanyEmail(value);
    this.value = value;
  }

  private void validateCompanyEmail(String companyEmail) {
    if (!Patterns.EMAIL_PATTERN.matcher(companyEmail).matches()) {
      throw new IllegalArgumentException("올바르지 않은 이메일입니다.");
    }
  }

}
