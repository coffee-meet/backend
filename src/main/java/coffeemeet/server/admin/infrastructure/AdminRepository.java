package coffeemeet.server.admin.infrastructure;

import coffeemeet.server.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {

}
