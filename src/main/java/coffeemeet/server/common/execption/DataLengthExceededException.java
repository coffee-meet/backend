package coffeemeet.server.common.execption;

public class DataLengthExceededException extends CoffeeMeetException {

  public DataLengthExceededException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

}
