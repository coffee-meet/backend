package coffeemeet.server.inquiry.service;

import coffeemeet.server.inquiry.domain.Inquiry;
import coffeemeet.server.inquiry.implement.InquiryCommand;
import coffeemeet.server.inquiry.implement.InquiryQuery;
import coffeemeet.server.inquiry.service.dto.InquiryDetailDto;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse.InquirySummary;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {

  private final InquiryCommand inquiryCommand;
  private final InquiryQuery inquiryQuery;
  private final UserQuery userQuery;

  public void registerInquiry(Long inquirerId, String title, String content) {
    inquiryCommand.createReport(new Inquiry(inquirerId, title, content));
  }

  public InquirySearchResponse searchInquiries(Long lastInquiryId, int pageSize) {
    List<Inquiry> inquiries = inquiryQuery.getInquiriesBy(lastInquiryId, pageSize);
    Map<Long, Profile> userMap = getUserProfiles(inquiries);
    List<InquirySummary> inquirySummaries = inquiries.stream()
        .map(inquiry -> InquirySummary.of(inquiry, userMap.get(inquiry.getInquirerId())))
        .toList();
    boolean hasNext = true;
    if (inquiries.size() < pageSize) {
      hasNext = false;
    }
    return InquirySearchResponse.of(inquirySummaries, hasNext);
  }

  private Map<Long, Profile> getUserProfiles(List<Inquiry> inquiries) {
    Set<Long> inquirerIds = inquiries.stream()
        .map(Inquiry::getInquirerId)
        .collect(Collectors.toUnmodifiableSet());
    Set<User> users = userQuery.getUsersByIdSet(inquirerIds);
    return users.stream()
        .collect(Collectors.toMap(User::getId, User::getProfile));
  }

  public InquiryDetailDto findInquiryBy(Long inquiryId) {
    Inquiry inquiry = inquiryQuery.getInquiryBy(inquiryId);
    User user = userQuery.getUserById(inquiry.getInquirerId());
    return InquiryDetailDto.of(inquiry, user);
  }

}
