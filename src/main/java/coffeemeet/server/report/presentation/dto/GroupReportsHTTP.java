package coffeemeet.server.report.presentation.dto;

import java.util.List;
import jakarta.validation.constraints.NotNull;

public final class GroupReportsHTTP {

    public record Request(
            @NotNull
            Long chattingRoomId,
            @NotNull
            Long targetedId
    ) {

    }

    public record Response(
            List<GroupReportHTTP.Response> reports
    ) {

        public static Response from(List<GroupReportHTTP.Response> response) {
            return new Response(response);
        }

    }

}
