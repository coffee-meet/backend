package coffeemeet.server.certification.infrastructure;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

  Optional<Certification> findByUserId(Long userId);

  boolean existsByCompanyEmail(CompanyEmail companyEmail);

  Page<Certification> findByIsCertificatedIsFalse(Pageable pageable);

}
