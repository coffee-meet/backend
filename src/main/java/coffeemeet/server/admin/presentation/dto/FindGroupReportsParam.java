package coffeemeet.server.admin.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record FindGroupReportsParam(
    @NotNull
    Long chattingRoomId,
    @NotNull
    Long targetedId
) {

}
