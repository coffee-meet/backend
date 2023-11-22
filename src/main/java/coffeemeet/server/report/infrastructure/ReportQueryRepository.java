package coffeemeet.server.report.infrastructure;

import coffeemeet.server.report.domain.QReport;
import coffeemeet.server.report.domain.Report;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

  public Page<Report> findAll(Pageable pageable) {
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

    List<Report> content = jpaQueryFactory
        .selectFrom(report)
        .where(report.id.in(subReports))
        .orderBy(report.createdAt.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = jpaQueryFactory
        .select(report.count())
        .from(report)
        .fetchOne();
    return new PageImpl<>(content, pageable, (total != null) ? total : 0);
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
