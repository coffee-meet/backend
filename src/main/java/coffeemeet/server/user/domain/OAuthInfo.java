package coffeemeet.server.user.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor
public class OAuthInfo {

  @Enumerated(value = EnumType.STRING)
  private OAuthProvider oauthProvider;

  private String oauthProviderId;

  public OAuthInfo(OAuthProvider oauthProvider, String oauthProviderId) {
    validateOAuthProviderId(oauthProviderId);
    this.oauthProvider = oauthProvider;
    this.oauthProviderId = oauthProviderId;
  }

  private void validateOAuthProviderId(String oauthProviderId) {
    if (!StringUtils.hasText(oauthProviderId)) {
      throw new IllegalArgumentException("올바르지 않은 로그인 아이디입니다.");
    }
  }

}
