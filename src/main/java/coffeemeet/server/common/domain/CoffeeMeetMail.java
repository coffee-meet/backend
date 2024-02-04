package coffeemeet.server.common.domain;

import coffeemeet.server.common.execption.GlobalErrorCode;
import coffeemeet.server.common.execption.InvalidInputException;
import io.micrometer.common.util.StringUtils;

public record CoffeeMeetMail(
    String receiver,
    String title,
    String contents
) {

  public CoffeeMeetMail {
    if (StringUtils.isBlank(receiver) || StringUtils.isBlank(title) || StringUtils.isBlank(
        contents)) {
      throw new InvalidInputException(GlobalErrorCode.VALIDATION_ERROR,
          String.format(
              "잘못된 메일 입력 값은 빈칸이나 null 일 수 없습니다. (sender: %s, title: %s, contents: %s)"
              , receiver, title, contents)
      );
    }
  }

}
