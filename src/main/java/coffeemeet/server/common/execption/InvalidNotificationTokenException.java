package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class InvalidNotificationTokenException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public InvalidNotificationTokenException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
