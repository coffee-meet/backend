package coffeemeet.server.report.infrastructure;

import coffeemeet.server.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

  boolean existsByReporterIdAndChattingRoomIdAndTargetedId(long reporterId, long chattingRoomId,
      long targetedId);
}
