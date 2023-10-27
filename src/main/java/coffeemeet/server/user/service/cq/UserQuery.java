package coffeemeet.server.user.service.cq;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.service.cq.CertificationQuery;
import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.dto.MyProfileDto;
import coffeemeet.server.user.dto.MyProfileDto.Response;
import coffeemeet.server.user.dto.UserProfileDto;
import coffeemeet.server.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQuery {

  private final UserRepository userRepository;
  private final InterestRepository interestRepository;
  private final CertificationQuery certificationQuery;

  public UserProfileDto.Response findUserProfile(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    List<Interest> interests = interestRepository.findAllByUserId(userId);
    List<Keyword> keywords = interests.stream().map(Interest::getKeyword).toList();
    Certification certification = certificationQuery.getCertificationByUserId(userId);

    return UserProfileDto.Response.of(user, certification.getDepartment(), keywords);
  }

  public Response findMyProfile(Long userId) {
    User user = getUserById(userId);
    List<Interest> interests = interestRepository.findAllByUserId(userId);
    List<Keyword> keywords = interests.stream().map(Interest::getKeyword).toList();
    Certification certification = certificationQuery.getCertificationByUserId(userId);

    return MyProfileDto.Response.of(user, keywords, certification.getDepartment());
  }

  public User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);
  }

  public void checkDuplicatedNickname(String nickname) {
    if (userRepository.findUserByProfileNickname(nickname).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
    }
  }

}
