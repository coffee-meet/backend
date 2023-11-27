package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.user.presentation.dto.UserStatusHTTP;
import coffeemeet.server.user.service.dto.UserStatusDto;

public class UserStatusHTTPFixture {
    public static UserStatusHTTP.Response userStatusHTTPResponse(UserStatusDto response) {
        return new UserStatusHTTP.Response(
                response.userStatus(),
                response.startedAt(),
                response.chattingRoomId(),
                response.isCertificated(),
                response.penaltyExpiration()
        );
    }

}
