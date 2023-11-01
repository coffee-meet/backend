package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.user.domain.Birth;
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

  private static Birth birth() {
    return Instancio.of(Birth.class)
        .generate(field(Birth::getBirthYear), gen -> gen.ints().range(1000, 9999).asString())
        .generate(field(Birth::getBirthDay), gen -> gen.ints().range(1000, 9999).asString())
        .create();
  }

  private static Profile profile() {
    return Instancio.of(Profile.class)
        .set(field(Profile::getBirth), birth())
        .set(field(Profile::getEmail), new Email("test123@gmail.com"))
        .generate(field(Profile::getNickname), gen -> gen.string().maxLength(20))
        .create();
  }

}
