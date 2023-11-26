package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.service.dto.ReportDto;
import org.instancio.Instancio;

public class ReportDtoFixture {

  public static ReportDto.Response reportDto() {
    return Instancio.of(ReportDto.Response.class)
        .create();
  }

}
