package coffeemeet.server.common.execption;

public class MissMatchException extends CoffeeMeetException {

  public MissMatchException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

}
