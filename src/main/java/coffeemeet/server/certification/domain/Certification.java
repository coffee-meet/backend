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

  @Embedded
  @Column(nullable = false)
  private CompanyEmail companyEmail;

  @Column(nullable = false)
  private String businessCardUrl;

  @Enumerated(EnumType.STRING)
  private Department department;

  @Column(nullable = false)
  private boolean isCertificated;

  @Builder
  private Certification(
      @NonNull CompanyEmail companyEmail, @NonNull String businessCardUrl,
      @NonNull Department department, @NonNull User user) {
    this.companyEmail = companyEmail;
    this.businessCardUrl = businessCardUrl;
    this.department = department;
    this.isCertificated = false;
    this.user = user;
  }

}
