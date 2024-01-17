package coffeemeet.server.common.config;

import coffeemeet.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {

  private final UserService userService;

  @Scheduled(cron = "0 3 * * *")
  public void checkUserDeleted() {
    userService.deleteUserInfos();
  }

}
