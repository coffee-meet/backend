package coffeemeet.server.common.execption;

public class DuplicatedDataException extends CoffeeMeetException {

  public DuplicatedDataException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

}
