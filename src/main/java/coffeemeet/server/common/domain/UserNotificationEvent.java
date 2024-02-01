package coffeemeet.server.common.domain;

public record UserNotificationEvent(
    Long userId,
    String message
) {

}
