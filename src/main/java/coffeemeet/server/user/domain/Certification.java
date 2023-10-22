package coffeemeet.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor
public class Certification {

  @Embedded
  @Column(nullable = false)
  private CompanyEmail companyEmail;

  @Column(nullable = false)
  private String businessCardUrl;

  @Column(nullable = false)
  private boolean isCertificated;

  @Column(nullable = false)
  private String department;

  public Certification(CompanyEmail companyEmail, String businessCardUrl, String department) {
    validateBusinessCardUrl(businessCardUrl);
    validateDepartment(department);
    this.companyEmail = companyEmail;
    this.businessCardUrl = businessCardUrl;
    this.department = department;
  }

  private void validateBusinessCardUrl(String businessCardUrl) {
    if (!StringUtils.hasText(businessCardUrl)) {
      throw new IllegalArgumentException("올바르지 않은 명함 url입니다.");
    }
  }

  private void validateDepartment(String department) {
    if (!StringUtils.hasText(department)) {
      throw new IllegalArgumentException("올바르지 않은 부서 이름입니다.");
    }
  }

  public void certificate() {
    isCertificated = true;
  }

}
