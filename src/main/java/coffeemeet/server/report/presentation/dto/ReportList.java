package coffeemeet.server.report.presentation.dto;

import coffeemeet.server.report.service.dto.ReportDto;
import java.time.LocalDateTime;

public record ReportList(
        String targetedNickname,
        String chattingRoomName,
        LocalDateTime createdAt,
        boolean hasNext
) {

    public static ReportList from(ReportDto.Response response) {
        return new ReportList(
                response.targetedNickname(),
                response.chattingRoomName(),
                response.createdAt(),
                response.hasNext()
        );
    }

}
