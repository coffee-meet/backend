package coffeemeet.server.common.execption;

public class ForbiddenException extends CoffeeMeetException {

  public ForbiddenException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
