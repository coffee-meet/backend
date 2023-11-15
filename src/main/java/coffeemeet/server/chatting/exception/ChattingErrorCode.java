package coffeemeet.server.chatting.exception;

import coffeemeet.server.common.execption.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChattingErrorCode implements ErrorCode {
  INVALID_MESSAGE("CM000", "유효하지 않은 메세지 형식입니다."),
  SOCKET_SESSION_NOT_FOUND("CM004", "사용자 소켓 연결 정보를 찾을 수 없습니다."),
  CHATTING_ROOM_NOT_FOUND("CR004", "채팅방을 찾을 수 없습니다.");

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
