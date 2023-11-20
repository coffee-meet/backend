package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.report.domain.Report;
import org.instancio.Instancio;

public class ReportFixture {

  public static Report report() {
    return Instancio.of(Report.class)
        .generate(field(Report::getReasonDetail), gen -> gen.string().maxLength(200)).create();
  }

  public static Report report(long targetId, long chattingRoomId) {
    return Instancio.of(Report.class)
        .set(field(Report::getTargetedId), targetId)
        .set(field(Report::getChattingRoomId), chattingRoomId)
        .generate(field(Report::getReasonDetail), gen -> gen.string().maxLength(200)).create();
  }

}
