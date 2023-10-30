package coffeemeet.server.common.execption.exception;

import lombok.Getter;

@Getter
public class MissMatchException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public MissMatchException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
