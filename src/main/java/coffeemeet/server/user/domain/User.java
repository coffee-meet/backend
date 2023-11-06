package coffeemeet.server.user.domain;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.common.domain.AdvancedBaseEntity;
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
import lombok.NonNull;
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
  @Column(nullable = false)
  private ReportInfo reportInfo;

  @Column(nullable = false)
  private boolean isDeleted;

  public User(
      @NonNull OAuthInfo oauthInfo,
      @NonNull Profile profile
  ) {
    this.oauthInfo = oauthInfo;
    this.profile = profile;
    this.reportInfo = new ReportInfo();
    this.isDeleted = false;
  }

  public void updateProfileImageUrl(String newProfileImageUrl) {
    this.profile.updateProfileImageUrl(newProfileImageUrl);
  }

  public void updateNickname(String newNickname) {
    this.profile.updateNickname(newNickname);
  }

}
