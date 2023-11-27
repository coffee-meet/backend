package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.service.dto.ReportDto;
import org.instancio.Instancio;

public class ReportDtoFixture {

  public static ReportDto reportDto() {
    return Instancio.of(ReportDto.class)
        .create();
  }

}
