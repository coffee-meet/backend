package coffeemeet.server.inquiry.service.dto;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.user.domain.User;
import java.time.LocalDateTime;

public record InquiryDetailDto(
    Long inquirerId,
    String inquirerNickname,
    String inquirerEmail,
    String title,
    String content,
    LocalDateTime createAt
) {

  public static InquiryDetailDto of(Inquiry inquiry, User user) {
    return new InquiryDetailDto(
        inquiry.getInquirerId(),
        user.getProfile().getNickname(),
        user.getOauthInfo().getEmail().getValue(),
        inquiry.getTitle(),
        inquiry.getContent(),
        inquiry.getCreatedAt()
    );
  }

}
