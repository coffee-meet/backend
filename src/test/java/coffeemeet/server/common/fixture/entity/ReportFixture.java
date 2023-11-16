package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.user.domain.Email;
import org.instancio.Instancio;

public class ReportFixture {

  public static Report report() {
    return Instancio.of(Report.class)
        .set(field(Report::getReporterEmail), new Email("test123@gmail.com"))
        .generate(field(Report::getReasonDetail), gen -> gen.string().maxLength(200)).create();
  }

}
