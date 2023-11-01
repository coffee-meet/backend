package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.certification.controller.dto.EmailDto;
import coffeemeet.server.certification.controller.dto.VerificationCodeDto;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.domain.EmailVerification;
import coffeemeet.server.user.domain.User;
import org.instancio.Instancio;
import org.instancio.internal.generator.domain.internet.EmailGenerator;
import org.instancio.internal.generator.lang.IntegerGenerator;
import org.instancio.internal.generator.net.URLGenerator;

public class CertificationFixture {

  public static Certification certification() {
    return Instancio.of(Certification.class)
        .generate(field(Certification::getBusinessCardUrl), gen -> gen.net().url().asString())
        .set(field(Certification::getCompanyEmail), new CompanyEmail(new EmailGenerator().get()))
        .create();
  }

  public static Certification certification(User user) {
    return Instancio.of(Certification.class)
        .generate(field(Certification::getBusinessCardUrl), gen -> gen.net().url().asString())
        .set(field(Certification::getCompanyEmail), new CompanyEmail(new EmailGenerator().get()))
        .set(field(Certification::getId), user.getId()).set(field(Certification::getUser), user)
        .create();
  }

  public static EmailVerification emailVerification() {
    return Instancio.of(EmailVerification.class)
        .set(field(EmailVerification::getCompanyEmail),
            new CompanyEmail(new EmailGenerator().get()))
        .create();
  }

  public static EmailVerification emailVerification(Long userId) {
    return Instancio.of(EmailVerification.class).set(field(EmailVerification::getUserId), userId)
        .set(field(EmailVerification::getCompanyEmail),
            new CompanyEmail(new EmailGenerator().get()))
        .create();
  }

  public static CompanyEmail companyEmail() {
    return Instancio.of(CompanyEmail.class)
        .generate(field(CompanyEmail::getValue), gen -> gen.net().email()).create();
  }

  public static String verificationCode() {
    return String.format("%06d", new IntegerGenerator().range(0, 999999).get());
  }

  public static String businessCardUrl() {
    return String.valueOf(new URLGenerator().get());
  }

  public static String email() {
    return new EmailGenerator().get();
  }

  public static Department department() {
    return Instancio.create(Department.class);
  }

  public static EmailDto.Request emailDtoRequest() {
    return Instancio.of(EmailDto.Request.class)
        .generate(field(EmailDto.Request::companyEmail), gen -> gen.net().email()).create();
  }

  public static VerificationCodeDto.Request verificationCodeDtoRequest() {
    return Instancio.of(VerificationCodeDto.Request.class)
        .set(field(VerificationCodeDto.Request::verificationCode),
            String.format("%06d", new IntegerGenerator().range(0, 999999).get())).create();
  }

}
