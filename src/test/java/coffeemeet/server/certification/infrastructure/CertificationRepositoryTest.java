package coffeemeet.server.certification.infrastructure;

import static coffeemeet.server.common.fixture.entity.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("유저 아이디로 회사 인증 정보를 조회할 수 있다.")
  void findByUserIdTest() {
    // given
    Certification certification = certification(user);
    certificationRepository.save(certification);

    // when, then
    assertThat(certificationRepository.findByUserId(user.getId())).isPresent();
  }

  @Test
  @DisplayName("존재하는 회사 이메일인지 확인할 수 있다.")
  void existsByCompanyEmailTest() {
    // given
    Certification certification = certification(user);
    certificationRepository.save(certification);
    // when, then
    assertThat(
        certificationRepository.existsByCompanyEmail(certification.getCompanyEmail())).isTrue();
  }

}
