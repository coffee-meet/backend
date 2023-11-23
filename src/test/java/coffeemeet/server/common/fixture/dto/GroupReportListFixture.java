package coffeemeet.server.common.fixture.dto;

import java.util.List;
import coffeemeet.server.report.presentation.dto.GroupReportList;
import coffeemeet.server.report.service.dto.GroupReportDto;

public class GroupReportListFixture {

    public static GroupReportList groupReportListResponse(List<GroupReportDto.Response> response) {
        return new GroupReportList(response);
    }

}
