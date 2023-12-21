package coffeemeet.server.common.execption;

public class LimitExceededException extends CoffeeMeetException {

  public LimitExceededException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
