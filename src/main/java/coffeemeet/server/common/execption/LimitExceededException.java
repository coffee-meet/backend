package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class LimitExceededException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public LimitExceededException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
