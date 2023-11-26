package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.service.dto.ReportDetailDto;
import org.instancio.Instancio;

public class ReportDetailDtoFixture {

  public static ReportDetailDto.Response reportDetailDto() {
    return Instancio.of(ReportDetailDto.Response.class)
        .create();
  }

}
