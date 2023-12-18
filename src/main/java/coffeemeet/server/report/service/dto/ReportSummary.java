package coffeemeet.server.report.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

<<<<<<<<HEAD:src/main/java/coffeemeet/server/report/service/dto/ReportSummary.java

public record ReportSummary(
========
public record Report(
    >>>>>>>>e82e9257(refactor:네이밍 변경):src/main/java/coffeemeet/server/report/service/dto/Report.java
    String targetedNickname,
    String chattingRoomName,
    Long targetedId,
    Long chattingRoomId,
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
        ){

        <<<<<<<<HEAD:src/main/java/coffeemeet/server/report/service/dto/ReportSummary.java
public static ReportSummary of(User targeted,ChattingRoom chattingRoom){
    return new ReportSummary(
    ========
public static Report of(User targeted,ChattingRoom chattingRoom){
    return new Report(
    >>>>>>>>e82e9257(refactor:네이밍 변경):src/main/java/coffeemeet/server/report/service/dto/Report.java
    targeted.getProfile().getNickname(),
    chattingRoom.getName(),
    targeted.getId(),
    chattingRoom.getId(),
    targeted.getCreatedAt()
    );
    }

    }
