package coffeemeet.server.common.execption;

public class BadRequestException extends CoffeeMeetException {

  public BadRequestException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

}
