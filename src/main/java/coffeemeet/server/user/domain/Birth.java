package coffeemeet.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor
public class Birth {

  private static final int BIRTH_LENGTH = 4;

  @Column(nullable = false, length = 4)
  String year;

  @Column(nullable = false, length = 4)
  String day;

  public Birth(String year, String day) {
    validateYear(year);
    validateDay(day);
    this.year = year;
    this.day = day;
  }

  private void validateYear(String year) {
    if (!StringUtils.hasText(year) || year.length() != BIRTH_LENGTH) {
      throw new IllegalArgumentException("올바르지 않은 연도 형식입니다.");
    }
  }

  private void validateDay(String day) {
    if (!StringUtils.hasText(day) || day.length() != BIRTH_LENGTH) {
      throw new IllegalArgumentException("올바르지 않은 날짜 형식입니다.");
    }
  }

}
