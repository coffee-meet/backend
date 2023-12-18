package coffeemeet.server.certification.service.dto;

import coffeemeet.server.certification.domain.Certification;

public record PendingCertificationDto(
    Long certificationId,
    String nickname,
    String companyName,
    String companyEmail,
    String businessCardUrl,
    String department
) {

  public static PendingCertificationDto from(Certification certification) {
    return new PendingCertificationDto(
        certification.getId(),
        certification.getUser().getProfile().getNickname(),
        certification.getCompanyName(),
        certification.getCompanyEmail().getValue(),
        certification.getBusinessCardUrl(),
        certification.getDepartment().name()
    );
  }

}
