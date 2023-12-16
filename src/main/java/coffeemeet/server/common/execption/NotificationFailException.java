package coffeemeet.server.common.execption;

public class NotificationFailException extends CoffeeMeetException {

  public NotificationFailException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

}
