package coffeemeet.server.report.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record FindGroupReports(
    @NotNull
    Long chattingRoomId,
    @NotNull
    Long targetedId
) {

}
