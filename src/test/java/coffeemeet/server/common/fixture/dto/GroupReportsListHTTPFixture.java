package coffeemeet.server.common.fixture.dto;

import java.util.List;
import coffeemeet.server.report.presentation.dto.GroupReportsListHTTP;
import coffeemeet.server.report.presentation.dto.TargetReportHTTP;

public class GroupReportsListHTTPFixture {

    public static GroupReportsListHTTP.Response myProfileHTTPResponse(List<TargetReportHTTP.Response> responses) {
        return new GroupReportsListHTTP.Response(responses);
    }

}
