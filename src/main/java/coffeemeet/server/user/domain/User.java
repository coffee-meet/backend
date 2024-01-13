package coffeemeet.server.user.domain;

import static coffeemeet.server.common.execption.GlobalErrorCode.BAD_REQUEST_ERROR;
import static coffeemeet.server.user.domain.UserStatus.CHATTING_CONNECTED;
import static coffeemeet.server.user.domain.UserStatus.CHATTING_UNCONNECTED;
import static coffeemeet.server.user.domain.UserStatus.IDLE;
import static coffeemeet.server.user.domain.UserStatus.MATCHING;
import static coffeemeet.server.user.domain.UserStatus.REPORTED;
import static java.time.LocalDateTime.now;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.common.domain.AdvancedBaseEntity;
import coffeemeet.server.common.execption.BadRequestException;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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

  private static final String INVALID_USER_STATUS = "올바르지 않은 유저 상태입니다.";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @Column(nullable = false)
  private OAuthInfo oauthInfo;

  @Embedded
  private Profile profile;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chatting_room_id")
  private ChattingRoom chattingRoom;

  @Embedded
  private ReportInfo reportInfo;

  @Embedded
  private NotificationInfo notificationInfo;

  @Enumerated(EnumType.STRING)
  private UserStatus userStatus;

  private boolean isDeleted;

  private LocalDate privacyDate;

  private boolean isBlacklisted;

  @Column(nullable = false)
  private boolean isRegistered;

  public User(@NonNull OAuthInfo oauthInfo) {
    this.oauthInfo = oauthInfo;
  }

  public void registerUser(@NonNull Profile profile) {
    this.profile = profile;
    this.reportInfo = new ReportInfo();
    this.userStatus = IDLE;
    this.isDeleted = false;
    this.privacyDate = null;
    this.isBlacklisted = false;
    this.isRegistered = true;
  }

  public void updateProfileImageUrl(@NonNull String newProfileImageUrl) {
    this.oauthInfo.updateProfileImageUrl(newProfileImageUrl);
  }

  public void updateNickname(@NonNull String newNickname) {
    this.profile.updateNickname(newNickname);
  }

  public void updateNotificationInfo(@NonNull NotificationInfo newNotificationInfo) {
    this.notificationInfo = newNotificationInfo;
  }

  public void completeMatching(ChattingRoom chattingRoom) {
    if (this.userStatus != MATCHING) {
      throw new BadRequestException(BAD_REQUEST_ERROR, INVALID_USER_STATUS);
    }
    this.userStatus = CHATTING_UNCONNECTED;
    this.chattingRoom = chattingRoom;
  }

  public void deleteChattingRoom() {
    this.chattingRoom = null;
  }

  public void enterChattingRoom() {
    if (this.userStatus != CHATTING_UNCONNECTED) {
      throw new BadRequestException(BAD_REQUEST_ERROR, INVALID_USER_STATUS);
    }
    this.userStatus = CHATTING_CONNECTED;
  }

  public void exitChattingRoom() {
    if (this.userStatus != CHATTING_CONNECTED) {
      throw new BadRequestException(BAD_REQUEST_ERROR, INVALID_USER_STATUS);
    }
    this.userStatus = CHATTING_UNCONNECTED;
  }

  public void setIdleStatus() {
    this.userStatus = IDLE;
  }

  public void punished() {
    this.userStatus = REPORTED;
    reportInfo.increment();
  }

  public void matching() {
    if (this.userStatus != IDLE) {
      throw new BadRequestException(BAD_REQUEST_ERROR, INVALID_USER_STATUS);
    }
    this.userStatus = MATCHING;
  }

  public void convertToBlacklist() {
    this.isBlacklisted = true;
  }

  public void delete() {
    this.isDeleted = true;
    this.privacyDate = LocalDate.from(now().plusDays(30));
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass =
        (o instanceof HibernateProxy) ? ((HibernateProxy) o).getHibernateLazyInitializer()
            .getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
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

}
