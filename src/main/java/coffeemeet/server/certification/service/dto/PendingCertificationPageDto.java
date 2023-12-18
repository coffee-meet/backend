package coffeemeet.server.certification.service.dto;

import org.springframework.data.domain.Page;

public record PendingCertificationPageDto(
    Page<PendingCertificationDto> page
) {

  public static PendingCertificationPageDto from(Page<PendingCertificationDto> page) {
    return new PendingCertificationPageDto(page);
  }

}
