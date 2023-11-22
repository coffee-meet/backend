package coffeemeet.server.common.fixture.dto;

import java.util.List;
import coffeemeet.server.report.presentation.dto.GroupReportHTTP;
import coffeemeet.server.report.presentation.dto.GroupReportsHTTP;

public class GroupReportsHTTPFixture {

    public static GroupReportsHTTP.Response myProfileHTTPResponse(List<GroupReportHTTP.Response> responses) {
        return new GroupReportsHTTP.Response(responses);
    }

}
