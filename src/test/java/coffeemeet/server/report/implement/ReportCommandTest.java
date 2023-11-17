package coffeemeet.server.report.implement;

import static coffeemeet.server.common.fixture.entity.ReportFixture.report;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.infrastructure.ReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportCommandTest {

  @InjectMocks
  private ReportCommand reportCommand;

  @Mock
  private ReportRepository reportRepository;

  @Test
  @DisplayName("신고를 생성해 저장할 수 있다.")
  void saveTest() {
    // given
    Report report = report();

    given(reportRepository.save(any(Report.class))).willReturn(report);

    // when, then
    assertThatCode(() -> reportCommand.save(report)).doesNotThrowAnyException();
  }

}
