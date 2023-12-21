package coffeemeet.server.certification.domain;

import coffeemeet.server.common.domain.AdvancedBaseEntity;
import coffeemeet.server.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@Table(name = "certifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certification extends AdvancedBaseEntity {

  @Id
  private Long id;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY)
  private User user;

  @Column(nullable = false)
  private String companyName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Department department;

  @Embedded
  private CompanyEmail companyEmail;

  @Column(nullable = false)
  private String businessCardUrl;

  @Column(nullable = false)
  private boolean isCertificated;

  @Builder
  private Certification(
      @NonNull String companyName, @NonNull CompanyEmail companyEmail,
      @NonNull String businessCardUrl, @NonNull Department department, @NonNull User user) {
    this.companyName = companyName;
    this.companyEmail = companyEmail;
    this.businessCardUrl = businessCardUrl;
    this.department = department;
    this.isCertificated = false;
    this.user = user;
  }

  public void qualify() {
    isCertificated = true;
  }

  public void update(String companyName, CompanyEmail companyEmail, String businessCardUrl,
      Department department) {
    this.companyName = companyName;
    this.companyEmail = companyEmail;
    this.businessCardUrl = businessCardUrl;
    this.department = department;
  }

}
