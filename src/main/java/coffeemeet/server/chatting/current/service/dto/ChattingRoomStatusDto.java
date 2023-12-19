package coffeemeet.server.chatting.current.service.dto;

public record ChattingRoomStatusDto(boolean isExisted) {

  public static ChattingRoomStatusDto from(boolean isExisted) {
    return new ChattingRoomStatusDto(isExisted);
  }

}
