package coffeemeet.server.report.infrastructure;

import coffeemeet.server.report.domain.Report;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {

  boolean existsByReporterIdAndChattingRoomIdAndTargetId(long reporterId, long chattingRoomId,
      long targetId);

  List<Report> findByTargetIdAndChattingRoomId(long targetId, long chattingRoomId);

  @Query("SELECT r FROM Report r WHERE r.createdAt IN (SELECT MAX(r2.createdAt) FROM Report r2 GROUP BY r2.targetId, r2.chattingRoomId)")
  List<Report> findAllReportsByTargetIdAndChattingRoomId();

}
