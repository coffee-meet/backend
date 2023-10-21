package coffeemeet.server.chatting_message_history.domain;

import coffeemeet.server.chatting_room_history.domain.ChattingRoomHistory;
import coffeemeet.server.common.entity.BaseEntity;
import jakarta.persistence.Column;
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
import org.springframework.util.StringUtils;

@Entity
@Getter
@Table(name = "chatting_message_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingMessageHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String message;

  @ManyToOne
  @JoinColumn(name = "chatting_room_history_id", nullable = false)
  private ChattingRoomHistory chattingRoomHistory;

  public ChattingMessageHistory(String message, ChattingRoomHistory chattingRoomHistory) {
    validateMessage(message);
    this.message = message;
    this.chattingRoomHistory = chattingRoomHistory;
  }

  private void validateMessage(String message) {
    if (!StringUtils.hasText(message)) {
      throw new IllegalArgumentException("올바르지 않은 메시지입니다.");
    }
  }

}
