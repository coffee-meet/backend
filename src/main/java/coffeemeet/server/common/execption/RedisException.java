package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class RedisException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public RedisException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
