package coffeemeet.server.common.config;

import coffeemeet.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulingConfig {

  private final UserService userService;

  @Scheduled(cron = "0 0 3 * * *")
  public void checkUserDeleted() {
    log.info("탈퇴 후 30일이 지난 회원 스케줄링 처리");
    userService.deleteUserInfos();
  }

}
