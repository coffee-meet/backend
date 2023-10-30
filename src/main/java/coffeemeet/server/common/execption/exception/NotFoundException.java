package coffeemeet.server.common.execption.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public NotFoundException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
