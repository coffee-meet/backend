package coffeemeet.server.report.infrastructure;

import static coffeemeet.server.report.domain.QReport.report;

import coffeemeet.server.report.domain.QReport;
import coffeemeet.server.report.domain.Report;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public Optional<Report> findById(long reportId) {
    QReport report = QReport.report;
    Report foundReport = jpaQueryFactory
        .selectFrom(report)
        .where(report.id.eq(reportId))
        .fetchOne();
    return Optional.ofNullable(foundReport);
  }

  public List<Report> findAll(Long lastReportId, int pageSize) {
    QReport report = QReport.report;
    QReport subReport = new QReport("subReport");

    List<Long> subReports = jpaQueryFactory
        .select(subReport.id)
        .from(subReport)
        .where(subReport.createdAt.in(
            JPAExpressions
                .select(subReport.createdAt.max())
                .from(subReport)
                .groupBy(subReport.targetedId, subReport.chattingRoomId)
        ))
        .fetch();

    return jpaQueryFactory
        .selectFrom(report)
        .where(report.id.in(subReports)
            .and(report.isProcessed.eq(false)
                .and(gtReportId(lastReportId))
            ))
        .orderBy(report.id.asc())
        .limit(pageSize)
        .fetch();
  }

  private BooleanExpression gtReportId(Long inquiryId) {
    if (inquiryId == null || inquiryId == 0L) {
      return null;
    }
    return report.id.gt(inquiryId);
  }

  public List<Report> findByTargetIdAndChattingRoomId(long targetId, long chattingRoomId) {
    QReport report = QReport.report;
    return jpaQueryFactory
        .selectFrom(report)
        .where(report.targetedId.eq(targetId).and(report.chattingRoomId.eq(chattingRoomId)))
        .orderBy(report.createdAt.desc())
        .fetch();
  }

}
