package coffeemeet.server.common.execption;

public class InvalidInputException extends CoffeeMeetException {

  public InvalidInputException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

}
