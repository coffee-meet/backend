package coffeemeet.server.certification.service.dto;

import org.springframework.data.domain.Page;

public record PendingCertificationPageDto(
    Page<PendingCertification> page
) {

}
