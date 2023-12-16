package coffeemeet.server.common.execption;

public class InvalidAuthException extends CoffeeMeetException {

  public InvalidAuthException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

}
