package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import org.instancio.Instancio;

public class UserFixture {

  public static User user() {
    return Instancio.of(User.class)
        .set(field(User::getProfile), profile())
        .ignore(field(User::isDeleted))
        .ignore(field(User::getChattingRoom))
        .create();
  }

  private static Profile profile() {
    return Instancio.of(Profile.class)
        .set(field(Profile::getEmail), new Email("test123@gmail.com"))
        .generate(field(Profile::getNickname), gen -> gen.string().maxLength(20))
        .create();
  }

}
