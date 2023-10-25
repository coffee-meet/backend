package coffeemeet.server.auth.service;

import coffeemeet.server.auth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import coffeemeet.server.auth.domain.client.OAuthMemberClientComposite;
import coffeemeet.server.auth.dto.OAuthInfoResponse;
import coffeemeet.server.auth.dto.SignupRequest;
import coffeemeet.server.auth.infrastructure.RefreshTokenRepository;
import coffeemeet.server.auth.utils.AuthTokens;
import coffeemeet.server.auth.utils.AuthTokensGenerator;
import coffeemeet.server.auth.utils.JwtTokenProvider;
import coffeemeet.server.interest.domain.Interest;
import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.interest.repository.InterestRepository;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.Profile;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.repository.UserRepository;
import coffeemeet.server.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final String EXPIRED_REFRESH_TOKEN_MESSAGE = "리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요.";
  private static final String ALREADY_REGISTERED_MESSAGE = "이미 가입된 사용자입니다.";
  private static final String USER_NOT_REGISTERED_MESSAGE = "해당 아이디(%s)와 로그인 타입(%s)의 유저는 회원가입되지 않았습니다.";

  private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
  private final OAuthMemberClientComposite oauthMemberClientComposite;
  private final UserRepository userRepository;
  private final InterestRepository interestRepository;
  private final AuthTokensGenerator authTokensGenerator;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;
  private final RefreshTokenRepository refreshTokenRepository;

  public String getAuthCodeRequestUrl(OAuthProvider oAuthProvider) {
    return authCodeRequestUrlProviderComposite.provide(oAuthProvider);
  }

  @Transactional
  public AuthTokens signup(SignupRequest request) {
    OAuthInfoResponse response = oauthMemberClientComposite.fetch(request.oAuthProvider(),
        request.authCode());
    checkDuplicateUser(response);
    String profileImage = checkProfileImage(response.profileImage());

    User user = new User(new OAuthInfo(response.oAuthProvider(), response.oAuthProviderId()),
        Profile.builder().name(response.name()).nickname(request.nickname()).email(response.email())
            .profileImageUrl(profileImage).birth(response.birth()).build());

    User newUser = userRepository.save(user);

    generateInterests(request, newUser);

    return authTokensGenerator.generate(newUser.getId());
  }

  public AuthTokens login(OAuthProvider oAuthProvider, String authCode) {
    OAuthInfoResponse response = oauthMemberClientComposite.fetch(oAuthProvider, authCode);
    User foundUser = userRepository.getUserByOauthInfoOauthProviderAndOauthInfoOauthProviderId(
        response.oAuthProvider(), response.oAuthProviderId()).orElseThrow(
        () -> new IllegalArgumentException(
            String.format(USER_NOT_REGISTERED_MESSAGE, response.oAuthProviderId(),
                response.oAuthProvider())));
    return authTokensGenerator.generate(foundUser.getId());
  }

  public AuthTokens renew(Long userId, String refreshToken) {
    if (jwtTokenProvider.isExpiredRefreshToken(refreshToken)) {
      throw new IllegalArgumentException(EXPIRED_REFRESH_TOKEN_MESSAGE);
    } else {
      return authTokensGenerator.reissueAccessToken(userId, refreshToken);
    }
  }

  public void logout(Long userId) {
    refreshTokenRepository.deleteById(userId);
  }

  @Transactional
  public void delete(Long userId) {
    userService.deleteUser(userId);
    refreshTokenRepository.deleteById(userId);
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
