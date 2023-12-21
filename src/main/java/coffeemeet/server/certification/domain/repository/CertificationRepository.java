package coffeemeet.server.certification.domain.repository;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

  Optional<Certification> findByUserId(Long userId);

  boolean existsByCompanyEmail(CompanyEmail companyEmail);

  void deleteByUserId(Long userId);

  @Query("SELECT c FROM Certification c JOIN FETCH c.user WHERE c.isCertificated = false")
  Page<Certification> findPendingCertifications(Pageable pageable);

}
