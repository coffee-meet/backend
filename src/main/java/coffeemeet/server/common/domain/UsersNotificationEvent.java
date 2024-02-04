package coffeemeet.server.common.domain;

import java.util.Set;

public record UsersNotificationEvent(
    Set<Long> userIds,
    String message
) {

}
