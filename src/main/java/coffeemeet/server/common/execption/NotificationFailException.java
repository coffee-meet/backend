package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class NotificationFailException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public NotificationFailException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
