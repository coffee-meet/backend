package coffeemeet.server.chatting.message.domain;

import static coffeemeet.server.chatting.exception.ChattingErrorCode.INVALID_MESSAGE;

import coffeemeet.server.common.domain.BaseEntity;
import coffeemeet.server.common.execption.InvalidInputException;
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
import lombok.NonNull;
import org.springframework.util.StringUtils;

@Entity
@Getter
@Table(name = "chatting_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingMessage extends BaseEntity {

  private static final String INVALID_CHATTING_MESSAGE = "입력된 (%s)는 올바르지 않은 메시지입니다.";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chatting_room_id", nullable = false)
  private ChattingRoom chattingRoom;

  public ChattingMessage(@NonNull String message, @NonNull ChattingRoom chattingRoom) {
    validateMessage(message);
    this.message = message;
    this.chattingRoom = chattingRoom;
  }

  private void validateMessage(String message) {
    if (!StringUtils.hasText(message)) {
      throw new InvalidInputException(INVALID_MESSAGE,
          String.format(INVALID_CHATTING_MESSAGE, message));
    }
  }

}
