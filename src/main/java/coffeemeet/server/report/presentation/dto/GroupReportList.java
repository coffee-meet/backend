package coffeemeet.server.report.presentation.dto;

import java.util.List;
import coffeemeet.server.report.service.dto.GroupReportDto;

public record GroupReportList(List<GroupReportDto.Response> reports) {

    public static GroupReportList from(List<GroupReportDto.Response> response) {
        return new GroupReportList(response);
    }

}

