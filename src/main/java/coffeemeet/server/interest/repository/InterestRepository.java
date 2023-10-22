package coffeemeet.server.interest.repository;

import coffeemeet.server.interest.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {

}
