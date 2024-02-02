package coffeemeet.server.chatting.current.domain;

public record ChattingMessageNotificationEvenet(
    Long roomId,
    String senderNickName,
    String message
) {

}
