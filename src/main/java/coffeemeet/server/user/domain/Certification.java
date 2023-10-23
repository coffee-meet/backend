package coffeemeet.server.user.domain;

import jakarta.persistence.AttributeOverride;
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
  @AttributeOverride(name = "email", column = @Column(name = "company_email"))
  private Email companyEmail;

  @Column(nullable = false)
  private String businessCardUrl;

  @Column(nullable = false)
  private boolean isCertificated;

  @Column(nullable = false)
  private String department;

  public Certification() {
    this.companyEmail = new Email(DEFAULT_EMAIL);
    this.businessCardUrl = DEFAULT;
    this.department = DEFAULT;
    isCertificated = false;
  }

  public void updateBusinessCardUrl(String newBusinessCardUrl) {
    this.businessCardUrl = newBusinessCardUrl;
  }

  public void certificate() {
    isCertificated = true;
  }

}
