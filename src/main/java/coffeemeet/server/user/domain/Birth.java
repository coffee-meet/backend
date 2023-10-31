package coffeemeet.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Birth {

  private static final int BIRTH_LENGTH = 4;

  @Column(nullable = false, length = BIRTH_LENGTH)
  String birthYear;

  @Column(nullable = false, length = BIRTH_LENGTH)
  String birthDay;

  public Birth(@NonNull String birthYear, @NonNull String birthDay) {
    validateYear(birthYear);
    validateDay(birthDay);
    this.birthYear = birthYear;
    this.birthDay = birthDay;
  }

  private void validateYear(String birthYear) {
    if (!StringUtils.hasText(birthYear) || birthYear.length() != BIRTH_LENGTH) {
      throw new IllegalArgumentException("올바르지 않은 연도 형식입니다.");
    }
  }

  private void validateDay(String birthDay) {
    if (!StringUtils.hasText(birthDay) || birthDay.length() != BIRTH_LENGTH) {
      throw new IllegalArgumentException("올바르지 않은 날짜 형식입니다.");
    }
  }

}
