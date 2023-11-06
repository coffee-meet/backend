package coffeemeet.server.certification.infrastructure;

import coffeemeet.server.certification.domain.EmailVerification;
import org.springframework.data.repository.CrudRepository;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, Long> {

}
