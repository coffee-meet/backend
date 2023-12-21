package coffeemeet.server.common.execption;

public class RedisException extends CoffeeMeetException {

  public RedisException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

}
