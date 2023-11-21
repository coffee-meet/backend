package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.service.dto.TargetReportDto;
import org.instancio.Instancio;

public class TargetReportDtoFixture {

  public static TargetReportDto.Response targetReportDto() {
    return Instancio.of(TargetReportDto.Response.class)
        .create();
  }

}
