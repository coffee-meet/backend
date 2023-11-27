package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.service.dto.GroupReportDto;
import org.instancio.Instancio;

public class GroupReportDtoFixture {

  public static GroupReportDto targetReportDto() {
    return Instancio.of(GroupReportDto.class)
        .create();
  }

}
