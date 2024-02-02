package coffeemeet.server.chatting.current.domain;

public record ChattingRoomNotificationEvent(
    Long chattingRoomId,
    String message
)
{

}
