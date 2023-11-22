package coffeemeet.server.report.presentation.dto;

import java.util.List;

public final class GroupReportsListHTTP {

    public record Response(
            List<TargetReportHTTP.Response> reports
    ) {

        public static Response from(List<TargetReportHTTP.Response> response) {
            return new Response(response);
        }
    }

}
