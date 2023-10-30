package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class MissMatchException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public MissMatchException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
