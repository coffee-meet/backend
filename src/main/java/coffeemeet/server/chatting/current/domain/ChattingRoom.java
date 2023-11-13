package coffeemeet.server.chatting.current.domain;

import coffeemeet.server.common.domain.BaseEntity;
import com.github.javafaker.Faker;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Random;
import lombok.Getter;

@Entity
@Getter
@Table(name = "chatting_rooms")
public class ChattingRoom extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  public ChattingRoom() {
    int randomNumber = new Random().nextInt(10000);
    String formattedRandomNumber = String.format("%04d", randomNumber);
    this.name = new Faker().food().fruit() + "-" + formattedRandomNumber;
  }

}
