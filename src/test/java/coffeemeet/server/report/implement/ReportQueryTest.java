package coffeemeet.server.report.implement;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.report.infrastructure.ReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportQueryTest {

  @InjectMocks
  private ReportQuery reportQuery;

  @Mock
  private ReportRepository reportRepository;

  @DisplayName("신고 내역 중복 체크를 할 수 있다.")
  @Test
  void hasDuplicatedReportTest() {
    // given
    long reporterId = 1L;
    long chattingRoomId = 1L;
    long targetId = 1L;

    given(reportRepository.existsByReporterIdAndChattingRoomIdAndTargetedId(anyLong(), anyLong(),
        anyLong())).willReturn(Boolean.FALSE);

    // when, then
    assertThatCode(() -> reportQuery.hasDuplicatedReport(reporterId, chattingRoomId, targetId))
        .doesNotThrowAnyException();
  }

}
