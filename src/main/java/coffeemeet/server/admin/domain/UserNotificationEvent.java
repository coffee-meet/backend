package coffeemeet.server.admin.domain;

public record UserNotificationEvent(
    Long userId,
    String message
) {

}
