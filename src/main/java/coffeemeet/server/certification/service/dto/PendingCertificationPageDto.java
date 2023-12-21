package coffeemeet.server.certification.service.dto;

import coffeemeet.server.certification.domain.Certification;
import org.springframework.data.domain.Page;

public record PendingCertificationPageDto(
    Page<PendingCertification> page
) {

  public static PendingCertificationPageDto from(
      Page<Certification> certificationPage
  ) {
    return new PendingCertificationPageDto(certificationPage.map(PendingCertification::from));
  }

}
