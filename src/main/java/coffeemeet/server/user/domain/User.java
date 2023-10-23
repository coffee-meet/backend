package coffeemeet.server.user.domain;

import coffeemeet.server.chatting_room.domain.ChattingRoom;
import coffeemeet.server.common.entity.AdvancedBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Table(name = "users")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AdvancedBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @Column(nullable = false)
  private OAuthInfo oauthInfo;

  @Embedded
  @Column(nullable = false)
  private Profile profile;

  @ManyToOne
  @JoinColumn(name = "chatting_room_id")
  private ChattingRoom chattingRoom;

  @Embedded
  private Certification certification;

  @Embedded
  @Column(nullable = false)
  private ReportInfo reportInfo;

  @Column(nullable = false)
  private boolean isDeleted;

  public User(
      OAuthInfo oauthInfo,
      Profile profile
  ) {
    this.oauthInfo = oauthInfo;
    this.profile = profile;
    this.certification = new Certification();
    this.reportInfo = new ReportInfo();
  }

  public void updateBusinessCardUrl(String newBusinessCardUrl) {
    certification.updateBusinessCardUrl(newBusinessCardUrl);
  }

  public void updateCompanyEmail(CompanyEmail newCompanyEmail) {
    certification.updateCompanyEmail(newCompanyEmail);
  }

  public void updateProfileImageUrl(String profileImageUrl) {
    this.profile.updateProfileImageUrl(profileImageUrl);
  }

}
