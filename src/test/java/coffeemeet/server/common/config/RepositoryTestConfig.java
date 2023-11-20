package coffeemeet.server.common.config;

import coffeemeet.server.chatting.current.infrastructure.ChattingMessageQueryRepository;
import coffeemeet.server.report.infrastructure.ReportQueryRepository;
import coffeemeet.server.chatting.history.infrastructure.ChattingMessageHistoryQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

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

  }

}
