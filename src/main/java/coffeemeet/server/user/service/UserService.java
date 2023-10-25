package coffeemeet.server.user.service;

import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.interest.service.InterestService;
import coffeemeet.server.user.domain.CompanyEmail;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.dto.MyProfileResponse;
import coffeemeet.server.user.dto.UserProfileResponse;
import coffeemeet.server.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  public static final String EXISTED_COMPANY_EMAIL_ERROR = "이미 사용 중인 회사이메일입니다.";

  private final UserRepository userRepository;
  private final InterestRepository interestRepository;
  private final InterestService interestService;

  @Transactional
  public void updateBusinessCardUrl(Long userId, String businessCardUrl) {
    User user = getUserById(userId);
    user.updateBusinessCardUrl(businessCardUrl);
  }

  public void validateDuplicatedCompanyEmail(CompanyEmail companyEmail) {
    if (userRepository.existsByCertification_CompanyEmail(companyEmail)) {
      throw new IllegalArgumentException(EXISTED_COMPANY_EMAIL_ERROR);
    }
  }

  public UserProfileResponse findUserProfile(String nickname) {
    User user = userRepository.findUserByProfileNickname(nickname)
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

    List<Interest> interests = interestRepository.findAllByUserId(user.getId());
    return UserProfileResponse.of(user, interests);
  }

  public MyProfileResponse findMyProfile(Long userId) {
    User user = getUserById(userId);
    List<Interest> interests = interestRepository.findAllByUserId(userId);
    return MyProfileResponse.of(user, interests);
  }

  @Transactional
  public void updateCompanyEmail(Long userId, CompanyEmail companyEmail) {
    User user = getUserById(userId);
    user.updateCompanyEmail(companyEmail);
  }

  @Transactional
  public void updateProfileImage(Long userId, String profileImageUrl) {
    User user = getUserById(userId);
    user.updateProfileImageUrl(profileImageUrl);
  }

  @Transactional
  public void updateProfileInfo(Long userId, String nickname, String name,
      List<Keyword> interests) {
    User user = getUserById(userId);

    user.updateNickname(nickname);
    user.updateName(name);

    interestService.updateInterests(userId, interests);
  }

  private User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);
  }

}
