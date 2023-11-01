package coffeemeet.server.certification.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyEmail {

  @Column(name = "company_email")
  private String value;

  public CompanyEmail(@NonNull String value) {
    this.value = value;
  }

}
