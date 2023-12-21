package coffeemeet.server.common.execption;

public class NotFoundException extends CoffeeMeetException {

  public NotFoundException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

}
