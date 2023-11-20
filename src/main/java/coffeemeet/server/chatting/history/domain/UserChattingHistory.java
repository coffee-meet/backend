package coffeemeet.server.chatting.history.domain;

import coffeemeet.server.common.domain.BaseEntity;
import coffeemeet.server.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Entity
@Getter
@Table(name = "user_chatting_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChattingHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chatting_room_history_id", nullable = false)
  private ChattingRoomHistory chattingRoomHistory;

  public UserChattingHistory(@NonNull User user, @NonNull ChattingRoomHistory chattingRoomHistory) {
    this.user = user;
    this.chattingRoomHistory = chattingRoomHistory;
  }

}
