package coffeemeet.server.certification.domain;

import coffeemeet.server.common.entity.AdvancedBaseEntity;
import coffeemeet.server.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @Column(nullable = false)
  private CompanyEmail companyEmail;

  @Column(nullable = false)
  private String businessCardUrl;

  @Enumerated(EnumType.STRING)
  private Department department;

  @Column(nullable = false)
  private boolean isCertificated;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true)
  private User user;

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
