package coffeemeet.server.auth.service;

import coffeemeet.server.auth.utils.AuthTokens;
import coffeemeet.server.auth.utils.AuthTokensGenerator;
import coffeemeet.server.auth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import coffeemeet.server.auth.domain.client.OAuthMemberClientComposite;
import coffeemeet.server.auth.domain.dto.OAuthInfoResponse;
import coffeemeet.server.auth.domain.dto.SignupRequest;
import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final String ALREADY_REGISTERED_MESSAGE = "이미 가입된 사용자입니다.";

  private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
  private final OAuthMemberClientComposite oauthMemberClientComposite;
  private final UserRepository userRepository;
  private final InterestRepository interestRepository;
  private final AuthTokensGenerator authTokensGenerator;

  public String getAuthCodeRequestUrl(OAuthProvider oAuthProvider) {
    return authCodeRequestUrlProviderComposite.provide(oAuthProvider);
  }

  public AuthTokens signup(SignupRequest request) {
    OAuthInfoResponse response = oauthMemberClientComposite.fetch(request.oAuthProvider(),
        request.authCode());
    checkDuplicateUser(response);
    String profileImage = checkProfileImage(response.profileImage());

    User user = new User(
        new OAuthInfo(
            response.oAuthProvider(),
            response.oAuthProviderId()
        ),
        Profile.builder()
            .name(response.name())
            .nickname(request.nickname())
            .email(response.email())
            .profileImageUrl(profileImage)
            .birth(response.birth())
            .build()
    );

    User newUser = userRepository.save(user);

    generateInterests(request, newUser);

    return authTokensGenerator.generate(newUser.getId());
  }

  private void checkDuplicateUser(OAuthInfoResponse response) {
    if (userRepository.existsUserByOauthInfo_oauthProviderAndOauthInfo_oauthProviderId(
        response.oAuthProvider(), response.oAuthProviderId())) {
      throw new IllegalArgumentException(ALREADY_REGISTERED_MESSAGE);
    }
  }

  private String checkProfileImage(String profileImage) {
    if (profileImage == null) {
      profileImage = "기본 이미지 URL";
    }
    return profileImage;
  }

  private void generateInterests(SignupRequest request, User newUser) {
    List<Interest> interests = new ArrayList<>();
    for (Keyword value : request.keywords()) {
      interests.add(new Interest(value, newUser));
    }
    interestRepository.saveAll(interests);
  }

}
