package coffeemeet.server.certification.domain.repository;

import static coffeemeet.server.common.fixture.CertificationFixture.certification;
import static coffeemeet.server.common.fixture.CertificationFixture.certificationPageable;
import static coffeemeet.server.common.fixture.UserFixture.user;
import static coffeemeet.server.common.fixture.UserFixture.usersWithNullId;
import static org.assertj.core.api.Assertions.assertThat;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.common.config.RepositoryTestConfig;
import coffeemeet.server.common.fixture.CertificationFixture;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class CertificationRepositoryTest extends RepositoryTestConfig {

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

  @Test
  @DisplayName("유저 아이디로 인증정보를 삭제할 수 있다")
  void deleteByUserIdTest() {
    // given
    Certification certification = certification(user);
    certificationRepository.save(certification);

    // when
    certificationRepository.deleteByUserId(user.getId());

    // then
    Optional<Certification> result = certificationRepository.findByUserId(user.getId());
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("아직 인증이 안된 회사 인증 요청을 페이지 조회할 수 있다.")
  void findPendingCertificationsTest() {
    // given
    List<User> users = userRepository.saveAll(usersWithNullId());

    List<Certification> certifications = certificationRepository.saveAll(
        users.stream()
            .map(CertificationFixture::certification)
            .toList()
    );

    Pageable pageable = certificationPageable();

    // when
    Page<Certification> foundCertification = certificationRepository.findPendingCertifications(
        pageable);

    // then
    assertThat(foundCertification.getSize()).isLessThanOrEqualTo(pageable.getPageSize());
  }

}
