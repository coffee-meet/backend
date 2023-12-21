package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class CoffeeMeetException extends RuntimeException {

  private final ErrorCode errorCode;

  public CoffeeMeetException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
