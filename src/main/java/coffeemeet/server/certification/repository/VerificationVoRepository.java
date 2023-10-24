package coffeemeet.server.certification.repository;

import coffeemeet.server.certification.domain.EmailVerification;
import org.springframework.data.repository.CrudRepository;

public interface VerificationVoRepository extends CrudRepository<EmailVerification, Long> {

}
