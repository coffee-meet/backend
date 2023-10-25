package coffeemeet.server.certification.repository;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

  Optional<Certification> findByUserId(Long userId);

  boolean existsByCompanyEmail(CompanyEmail companyEmail);

  boolean existsByUserId(Long userId);

}
