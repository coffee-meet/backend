package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class BadRequestException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public BadRequestException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
