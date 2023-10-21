package coffeemeet.server.chatting_message.domain;

import coffeemeet.server.chatting_room.domain.ChattingRoom;
import coffeemeet.server.common.entity.BaseEntity;
import jakarta.persistence.Column;
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
import org.springframework.util.StringUtils;

@Entity
@Getter
@Table(name = "chatting_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingMessage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chatting_room_id", nullable = false)
  private ChattingRoom chattingRoom;

  public ChattingMessage(String message, ChattingRoom chattingRoom) {
    validateMessage(message);
    this.message = message;
    this.chattingRoom = chattingRoom;
  }

  private void validateMessage(String message) {
    if (!StringUtils.hasText(message)) {
      throw new IllegalArgumentException("올바르지 않은 메시지입니다.");
    }
  }

}
