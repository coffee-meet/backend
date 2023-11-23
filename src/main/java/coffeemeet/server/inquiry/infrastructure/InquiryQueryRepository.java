package coffeemeet.server.inquiry.infrastructure;

import static coffeemeet.server.inquiry.domain.QInquiry.inquiry;

import coffeemeet.server.inquiry.domain.Inquiry;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public List<Inquiry> findAllInquiries(Long lastInquiryId, int pageSize) {
    return jpaQueryFactory
        .selectFrom(inquiry)
        .where(inquiry.isCheck.eq(false)
            .and(gtInquiryIdId(lastInquiryId)))
        .orderBy(inquiry.id.asc())
        .limit(pageSize)
        .fetch();
  }

  private BooleanExpression gtInquiryIdId(Long inquiryId) {
    if (inquiryId == null || inquiryId == 0L) {
      return null;
    }
    return inquiry.id.gt(inquiryId);
  }

}
