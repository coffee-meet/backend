package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.service.dto.ReportDetailDto;
import org.instancio.Instancio;

public class ReportDetailDtoFixture {

  public static ReportDetailDto reportDetailDto() {
    return Instancio.of(ReportDetailDto.class)
        .create();
  }

}
