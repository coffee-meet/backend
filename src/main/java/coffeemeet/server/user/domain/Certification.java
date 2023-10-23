package coffeemeet.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

@Getter
@Embeddable
public class Certification {

  private static final String DEFAULT_EMAIL = "default@default.com";
  private static final String DEFAULT = "default";

  @Embedded
  @Column(nullable = false)
  private CompanyEmail companyEmail;

  @Column(nullable = false)
  private String businessCardUrl;

  @Column(nullable = false)
  private boolean isCertificated;

  @Column(nullable = false)
  private String department;

  public Certification() {
    this.companyEmail = new CompanyEmail(DEFAULT_EMAIL);
    this.businessCardUrl = DEFAULT;
    this.department = DEFAULT;
    isCertificated = false;
  }

  public void updateBusinessCardUrl(String newBusinessCardUrl) {
    this.businessCardUrl = newBusinessCardUrl;
  }

  public void updateCompanyEmail(CompanyEmail newCompanyEmail) {
    this.companyEmail = newCompanyEmail;
  }

  public void certificate() {
    isCertificated = true;
  }

}
