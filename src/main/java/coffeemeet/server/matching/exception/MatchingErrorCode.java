package coffeemeet.server.matching.exception;


import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingErrorCode implements ErrorCode {
  NOT_CERTIFICATED_USER("M003", "인증되지 않은 사용자입니다."),
  ;

  private final String errorCode;
  private final String message;

  @Override
  public String code() {
    return errorCode;
  }

  @Override
  public String message() {
    return message;
  }

}
