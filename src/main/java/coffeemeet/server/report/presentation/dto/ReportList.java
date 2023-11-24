package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.ReportDto;
import java.util.List;

public record ReportList(
        List<ReportDto.Response> contents,
        boolean hasNext
) {

    public static ReportList of(List<ReportDto.Response> contents, boolean hasNext) {
        return new ReportList(
                contents,
                hasNext
        );
    }

}
