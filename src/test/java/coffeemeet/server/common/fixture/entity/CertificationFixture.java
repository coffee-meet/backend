package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.user.domain.User;
import org.instancio.Instancio;

public class CertificationFixture {

  public static Certification certification() {
    return Instancio.of(Certification.class)
        .set(field(Certification::getCompanyEmail), companyEmail())
        .create();
  }

  public static Certification certification(User user) {
    return Instancio.of(Certification.class)
        .set(field(Certification::getCompanyEmail), companyEmail())
        .set(field(Certification::getUser), user)
        .create();
  }

  private static CompanyEmail companyEmail() {
    return Instancio.of(CompanyEmail.class)
        .generate(field(CompanyEmail::getValue), gen -> gen.net().email())
        .create();
  }

}
