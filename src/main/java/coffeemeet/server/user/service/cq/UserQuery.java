package coffeemeet.server.user.service.cq;

import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQuery {

  private static final String USER_NOT_REGISTERED_MESSAGE = "해당 아이디(%s)와 로그인 타입(%s)의 유저는 회원가입되지 않았습니다.";
  private static final String ALREADY_REGISTERED_MESSAGE = "이미 가입된 사용자입니다.";

  private final UserRepository userRepository;

  public User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);
  }

  public User getUserByOAuthInfo(OAuthProvider oAuthProvider, String oAuthProviderId) {
    return userRepository.getUserByOauthInfoOauthProviderAndOauthInfoOauthProviderId(
        oAuthProvider, oAuthProviderId).orElseThrow(
        () -> new IllegalArgumentException(
            String.format(USER_NOT_REGISTERED_MESSAGE, oAuthProviderId,
                oAuthProvider)));
  }

  public void hasDuplicatedNickname(String nickname) {
    if (userRepository.existsUserByProfile_Nickname(nickname)) {
      throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
    }
  }

  public void hasDuplicatedUser(OAuthProvider oAuthProvider, String oAuthProviderId) {
    if (userRepository.existsUserByOauthInfo_oauthProviderAndOauthInfo_oauthProviderId(
        oAuthProvider, oAuthProviderId)) {
      throw new IllegalArgumentException(ALREADY_REGISTERED_MESSAGE);
    }
  }

}
