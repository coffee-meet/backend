package coffeemeet.server.inquiry.infrastructure;

import coffeemeet.server.inquiry.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

}
