package coffeemeet.server.common.execption;

import lombok.Getter;

@Getter
public class DuplicatedDataException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public DuplicatedDataException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
