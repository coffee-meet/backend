package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class ForbiddenException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public ForbiddenException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
