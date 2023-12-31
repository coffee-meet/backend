package coffeemeet.server.admin.domain;

import coffeemeet.server.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {

  @Id
  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String password;

  public boolean isCorrectPassword(String password) {
    return this.password.equals(password);
  }

}
