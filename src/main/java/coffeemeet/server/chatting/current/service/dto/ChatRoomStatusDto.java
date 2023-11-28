package coffeemeet.server.chatting.current.service.dto;

public record ChatRoomStatusDto(boolean isExisted) {

  public static ChatRoomStatusDto from(boolean isExisted) {
    return new ChatRoomStatusDto(isExisted);
  }

}
