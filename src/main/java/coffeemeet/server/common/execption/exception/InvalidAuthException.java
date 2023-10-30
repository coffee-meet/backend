package coffeemeet.server.common.execption.exception;

import lombok.Getter;

@Getter
public class InvalidAuthException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public InvalidAuthException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
