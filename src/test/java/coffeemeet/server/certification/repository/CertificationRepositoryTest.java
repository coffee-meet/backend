package coffeemeet.server.certification.repository;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CertificationRepositoryTest {

  @Autowired
  private CertificationRepository certificationRepository;
  @Autowired
  private UserRepository userRepository;
  private User user;


  @BeforeEach
  void setUp() {
    user = userRepository.save(user());
  }

  @Test
  void findByUserIdTest() {
    // given
    Certification certification = certification(user);
    certificationRepository.save(certification);

    // when, then
    assertThat(certificationRepository.findByUserId(user.getId())).isPresent();
  }

  @Test
  void existsByCompanyEmailTest() {
    // given
    Certification certification = certification(user);
    certificationRepository.save(certification);
    // when, then
    assertThat(
        certificationRepository.existsByCompanyEmail(certification.getCompanyEmail())).isTrue();
  }

}
