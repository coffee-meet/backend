package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class InvalidInputException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public InvalidInputException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
