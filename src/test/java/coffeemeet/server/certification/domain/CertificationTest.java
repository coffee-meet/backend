package coffeemeet.server.certification.domain;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.businessCardUrl;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.companyEmail;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.companyName;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.department;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CertificationTest {

  @Test
  @DisplayName("Certification을 업데이트 할 수 있다.")
  void updateTest() {
    // given
    Certification certification = certification();
    String newCompanyName = companyName();
    CompanyEmail newCompanyEmail = companyEmail();
    String newBusinessCardUrl = businessCardUrl();
    Department newDepartment = department();

    // when
    certification.update(newCompanyName, newCompanyEmail, newBusinessCardUrl, newDepartment);

    // then
    assertThat(certification).extracting(Certification::getCompanyName,
            Certification::getCompanyEmail, Certification::getBusinessCardUrl,
            Certification::getDepartment)
        .containsExactly(newCompanyName, newCompanyEmail, newBusinessCardUrl, newDepartment);
  }

}
