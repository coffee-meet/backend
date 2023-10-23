package coffeemeet.server.certification.repository;

import coffeemeet.server.certification.domain.VerificationCode;
import org.springframework.data.repository.CrudRepository;

public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {

}
