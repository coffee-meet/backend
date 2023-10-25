package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.user.domain.Birth;
import coffeemeet.server.user.domain.Certification;
import coffeemeet.server.user.domain.CompanyEmail;
import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import org.instancio.Instancio;

public class UserFixture {

  public static User user() {
    return Instancio.of(User.class)
        .set(field(User::getProfile), profile())
        .set(field(User::getCertification), certification())
        .ignore(field(User::isDeleted))
        .create();
  }

  private static Birth birth() {
    return Instancio.of(Birth.class)
        .generate(field(Birth::getBirthYear), gen -> gen.ints().range(1000, 9999))
        .generate(field(Birth::getBirthDay), gen -> gen.ints().range(1000, 9999))
        .create();
  }

  private static Profile profile() {
    return Instancio.of(Profile.class)
        .set(field(Profile::getBirth), birth())
        .set(field(Profile::getEmail), email())
        .generate(field(Profile::getNickname), gen -> gen.string().maxLength(20))
        .create();
  }

  private static Certification certification() {
    return Instancio.of(Certification.class)
        .set(field(Certification::getCompanyEmail), companyEmail())
        .create();
  }

  private static Email email() {
    return Instancio.of(Email.class)
        .generate(field(Email::getEmail), gen -> gen.net().email())
        .create();
  }

  private static CompanyEmail companyEmail() {
    return Instancio.of(CompanyEmail.class)
        .generate(field(CompanyEmail::getCompanyEmail), gen -> gen.net().email())
        .create();
  }

}
