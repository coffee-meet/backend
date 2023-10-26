package coffeemeet.server.user.service;

import static coffeemeet.server.common.media.S3MediaService.KeyType.PROFILE_IMAGE;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.AuthTokensGenerator;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.service.cq.CertificationQuery;
import coffeemeet.server.common.media.S3MediaService;
import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.interest.service.InterestService;
import coffeemeet.server.oauth.dto.OAuthInfoDto;
import coffeemeet.server.oauth.service.OAuthService;
import coffeemeet.server.user.domain.Birth;
import coffeemeet.server.user.domain.Email;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.dto.MyProfileResponse;
import coffeemeet.server.user.dto.SignupRequest;
import coffeemeet.server.user.dto.UserProfileResponse;
import coffeemeet.server.user.repository.UserRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private static final String ALREADY_REGISTERED_MESSAGE = "이미 가입된 사용자입니다.";
  private static final String USER_NOT_REGISTERED_MESSAGE = "해당 아이디(%s)와 로그인 타입(%s)의 유저는 회원가입되지 않았습니다.";
  private static final String DEFAULT_IMAGE_URL = "기본 이미지 URL";

  private final S3MediaService s3MediaService;
  private final InterestService interestService;
  private final OAuthService oAuthService;
  private final UserRepository userRepository;
  private final InterestRepository interestRepository;
  private final CertificationQuery certificationQuery;
  private final AuthTokensGenerator authTokensGenerator;

  public UserProfileResponse findUserProfile(long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    List<Interest> interests = interestRepository.findAllByUserId(userId);
    Certification certification = certificationQuery.getCertificationByUserId(userId);

    return UserProfileResponse.of(user, certification.getDepartment(), interests);
  }

  public MyProfileResponse findMyProfile(Long userId) {
    User user = getUserById(userId);
    List<Interest> interests = interestRepository.findAllByUserId(userId);
    Certification certification = certificationQuery.getCertificationByUserId(userId);

    return MyProfileResponse.of(user, interests, certification.getDepartment());
  }

  @Transactional
  public void updateProfileImage(Long userId, File file) {
    User user = getUserById(userId);
    deleteCurrentProfileImage(user);
    String key = s3MediaService.generateKey(PROFILE_IMAGE);
    s3MediaService.upload(key, file);
    user.updateProfileImageUrl(s3MediaService.getUrl(key));
  }

  private void deleteCurrentProfileImage(User user) {
    String currentKey = s3MediaService.extractKey(user.getProfile().getProfileImageUrl(),
        PROFILE_IMAGE);
    s3MediaService.delete(currentKey);
  }

  @Transactional
  public void updateProfileInfo(Long userId, String nickname, String name,
      List<Keyword> interests) {
    User user = getUserById(userId);

    checkDuplicatedNickname(nickname);

    user.updateNickname(nickname);
    user.updateName(name);

    interestService.updateInterests(userId, interests);
  }

  @Transactional
  public AuthTokens signup(SignupRequest request) {
    OAuthInfoDto.Response response = oAuthService.getOAuthInfo(request.oAuthProvider(),
        request.authCode());

    checkDuplicatedUser(response);
    checkDuplicatedNickname(request.nickname());
    String profileImage = getProfileImageOrDefault(response.profileImage());

    User user = new User(new OAuthInfo(response.oAuthProvider(), response.oAuthProviderId()),
        Profile.builder().name(response.name()).nickname(request.nickname())
            .email(new Email(response.email()))
            .profileImageUrl(profileImage)
            .birth(new Birth(response.birthYear(), response.birthDay())).build());

    User newUser = userRepository.save(user);
    saveInterests(request, newUser);
    return authTokensGenerator.generate(newUser.getId());
  }

  public AuthTokens login(OAuthProvider oAuthProvider, String authCode) {
    OAuthInfoDto.Response response = oAuthService.getOAuthInfo(oAuthProvider, authCode);
    User foundUser = userRepository.getUserByOauthInfoOauthProviderAndOauthInfoOauthProviderId(
        response.oAuthProvider(), response.oAuthProviderId()).orElseThrow(
        () -> new IllegalArgumentException(
            String.format(USER_NOT_REGISTERED_MESSAGE, response.oAuthProviderId(),
                response.oAuthProvider())));
    return authTokensGenerator.generate(foundUser.getId());
  }

  @Transactional
  public void deleteUser(Long userId) {
    interestRepository.deleteById(userId);
    userRepository.deleteById(userId);
  }

  public void checkDuplicatedNickname(String nickname) {
    if (userRepository.findUserByProfileNickname(nickname).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
    }
  }

  public void checkDuplicatedUser(OAuthInfoDto.Response response) {
    if (userRepository.existsUserByOauthInfo_oauthProviderAndOauthInfo_oauthProviderId(
        response.oAuthProvider(), response.oAuthProviderId())) {
      throw new IllegalArgumentException(ALREADY_REGISTERED_MESSAGE);
    }
  }

  public User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);
  }

  private String getProfileImageOrDefault(String profileImage) {
    if (profileImage == null) {
      profileImage = DEFAULT_IMAGE_URL;
    }
    return profileImage;
  }

  private void saveInterests(SignupRequest request, User newUser) {
    List<Interest> interests = new ArrayList<>();
    for (Keyword value : request.keywords()) {
      interests.add(new Interest(value, newUser));
    }
    interestRepository.saveAll(interests);
  }

}
