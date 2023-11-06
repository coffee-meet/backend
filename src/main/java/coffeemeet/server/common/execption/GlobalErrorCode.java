package coffeemeet.server.common.execption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

  VALIDATION_ERROR("G000", "유효하지 않은 입력입니다."),
  INVALID_FCM_TOKEN("G001", "유효하지 않은 FCM토큰입니다."),
  PUSH_NOTIFICATION_SEND_FAILURE("G002", "푸시 알림 전송에 실패했습니다."),
  INVALID_S3_URL("G004", "유효하지 않은 s3 URL 입니다."),
  STOMP_ACCESSOR_NOT_FOUND("G005", "웹소켓 연결을 할 수 없습니다."),
  INTERNAL_SERVER_ERROR("G050", "예상치 못한 오류입니다.");

  private final String code;
  private final String message;

  @Override
  public String code() {
    return this.code;
  }

  @Override
  public String message() {
    return this.message;
  }

}
