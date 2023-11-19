package coffeemeet.server.common.fixture.entity;

import static java.time.LocalDateTime.now;
import static org.instancio.Select.field;

import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.presentation.dto.NotificationTokenHTTP;
import java.util.List;
import java.util.Set;
import org.instancio.Instancio;

public class UserFixture {

  public static User user() {
    return Instancio.of(User.class).set(field(User::getProfile), profile())
        .ignore(field(User::isDeleted))
        .ignore(field(User::isBlacklisted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  public static List<User> users() {
    return Instancio.ofList(User.class).size(4)
        .generate(field(User::getId), gen -> gen.longSeq().start(1L))
        .create();
  }

  private static Profile profile() {
    return Instancio.of(Profile.class)
        .set(field(Profile::getEmail), new Email("test123@gmail.com"))
        .generate(field(Profile::getNickname), gen -> gen.string().maxLength(20)).create();
  }

  public static NotificationTokenHTTP.Request notificationTokenHTTPRequest() {
    return Instancio.create(NotificationTokenHTTP.Request.class);
  }

  public static List<Keyword> keywords() {
    return Instancio.ofList(Keyword.class).size(3)
        .create();
  }

  public static String token() {
    return Instancio.create(String.class);
  }

  public static NotificationInfo notificationInfo() {
    return Instancio.of(NotificationInfo.class)
        .set(field(NotificationInfo::isSubscribedToNotification), true)
        .generate(field(NotificationInfo::getCreatedTokenAt),
            gen -> gen.temporal().localDateTime().range(now().minusMonths(2), now())).create();
  }

  public static Set<NotificationInfo> notificationInfos() {
    return Instancio.ofSet(NotificationInfo.class)
        .set(field(NotificationInfo::isSubscribedToNotification), true)
        .generate(field(NotificationInfo::getCreatedTokenAt),
            gen -> gen.temporal().localDateTime().range(now().minusMonths(2), now())).create();
  }

  public static String content() {
    return Instancio.create(String.class);
  }

}
