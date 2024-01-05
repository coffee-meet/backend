package coffeemeet.server.common.config;

import coffeemeet.server.chatting.current.infrastructure.ChattingMessageQueryRepository;
import coffeemeet.server.chatting.history.infrastructure.ChattingMessageHistoryQueryRepository;
import coffeemeet.server.chatting.history.infrastructure.ChattingMessageHistoryRepositoryImpl;
import coffeemeet.server.inquiry.infrastructure.InquiryQueryRepository;
import coffeemeet.server.report.infrastructure.ReportQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
@Import(value = {QueryDslConfig.class, JPAConfig.class,
    RepositoryTestConfig.QueryRepositoryTestConfig.class})
public class RepositoryTestConfig {

  @TestConfiguration
  static public class QueryRepositoryTestConfig {

    @Bean
    public ChattingMessageQueryRepository chattingMessageQueryRepository(
        JPAQueryFactory jpaQueryFactory) {
      return new ChattingMessageQueryRepository(jpaQueryFactory);
    }

    @Bean
    public ReportQueryRepository reportQueryRepository(
        JPAQueryFactory jpaQueryFactory) {
      return new ReportQueryRepository(jpaQueryFactory);
    }

    @Bean
    public ChattingMessageHistoryQueryRepository chattingMessageHistoryQueryRepository(
        JPAQueryFactory jpaQueryFactory) {
      return new ChattingMessageHistoryQueryRepository(jpaQueryFactory);
    }

    @Bean
    public InquiryQueryRepository inquiryQueryRepository(
        JPAQueryFactory jpaQueryFactory) {
      return new InquiryQueryRepository(jpaQueryFactory);
    }

    @Bean
    public ChattingMessageHistoryRepositoryImpl chattingMessageHistoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
      return new ChattingMessageHistoryRepositoryImpl(jdbcTemplate);
    }

  }

}
