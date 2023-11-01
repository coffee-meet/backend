package coffeemeet.server.user.domain;

import static coffeemeet.server.user.exception.UserErrorCode.INVALID_BIRTH_DAY;
import static coffeemeet.server.user.exception.UserErrorCode.INVALID_BIRTH_YEAR;

import coffeemeet.server.common.execption.DataLengthExceededException;
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
  private static final String INVALID_BIRTH_YEAR_MESSAGE = "올바르지 않은 출생년도(%s)입니다.";
  private static final String INVALID_BIRTH_DAY_MESSAGE = "올바르지 않은 생년월일(%s)입니다.";

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
      throw new DataLengthExceededException(
          INVALID_BIRTH_YEAR,
          String.format(INVALID_BIRTH_YEAR_MESSAGE, birthYear)
      );
    }
  }

  private void validateDay(String birthDay) {
    if (!StringUtils.hasText(birthDay) || birthDay.length() != BIRTH_LENGTH) {
      throw new DataLengthExceededException(
          INVALID_BIRTH_DAY,
          String.format(INVALID_BIRTH_DAY_MESSAGE, birthDay)
      );
    }
  }

}
