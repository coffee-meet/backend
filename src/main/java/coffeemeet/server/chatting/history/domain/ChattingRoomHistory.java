package coffeemeet.server.chatting.history.domain;

import coffeemeet.server.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "chatting_room_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoomHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  public ChattingRoomHistory(@NonNull String name) {
    this.name = name;
  }

}
