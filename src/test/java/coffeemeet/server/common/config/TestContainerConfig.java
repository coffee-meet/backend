package coffeemeet.server.common.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainerConfig {

  private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
  private static final int REDIS_PORT = 6379;
  private static final GenericContainer redis;

  static {
    redis = new GenericContainer(REDIS_IMAGE)
        .withExposedPorts(REDIS_PORT)
        .withReuse(true);
    redis.start();
  }

  @DynamicPropertySource
  private static void registerRedisProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", () -> redis.getMappedPort(REDIS_PORT)
        .toString());
  }

}
