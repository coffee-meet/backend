package coffeemeet.server.user.infrastructure;

import coffeemeet.server.user.domain.Interest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {

  List<Interest> findAllByUserId(Long userId);
}
