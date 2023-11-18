package coffeemeet.server.user.domain;

import static coffeemeet.server.user.domain.UserStatus.CHATTING_CONNECTED;
import static coffeemeet.server.user.domain.UserStatus.CHATTING_UNCONNECTED;
import static coffeemeet.server.user.domain.UserStatus.IDLE;
import static coffeemeet.server.user.domain.UserStatus.REPORTED;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.common.domain.AdvancedBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Where;
import org.hibernate.proxy.HibernateProxy;

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

  @Embedded
  private NotificationInfo notificationInfo;

  @Enumerated(EnumType.STRING)
  private UserStatus userStatus;

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
    this.userStatus = IDLE;
  }

  public void updateProfileImageUrl(@NonNull String newProfileImageUrl) {
    this.profile.updateProfileImageUrl(newProfileImageUrl);
  }

  public void updateNickname(@NonNull String newNickname) {
    this.profile.updateNickname(newNickname);
  }

  public void updateNotificationInfo(@NonNull NotificationInfo newNotificationInfo) {
    this.notificationInfo = newNotificationInfo;
  }

  public void enterChattingRoom(ChattingRoom chattingRoom) {
    this.chattingRoom = chattingRoom;
  }

  public void enterChattingRoom() {
    this.userStatus = CHATTING_CONNECTED;
  }

  public void exitChattingRoom() {
    this.userStatus = CHATTING_UNCONNECTED;
  }

  public void setIdleStatus() {
    this.userStatus = IDLE;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = (o instanceof HibernateProxy)
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    User user = (User) o;
    return getId() != null && Objects.equals(getId(), user.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }

  public void punished() {
    this.userStatus = REPORTED;
    reportInfo.increment();
  }

}
