package coffeemeet.server.inquiry.service.dto;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.user.domain.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record InquirySummary(
    Long inquiryId,
    String inquirer,
    String title,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdAt
) {

  public static InquirySummary of(Inquiry inquiry, Profile profile) {
    return new InquirySummary(
        inquiry.getId(),
        profile.getNickname(),
        inquiry.getTitle(),
        inquiry.getCreatedAt()
    );
  }

}
