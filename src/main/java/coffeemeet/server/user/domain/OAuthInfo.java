package coffeemeet.server.user.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthInfo {

  @Enumerated(value = EnumType.STRING)
  private OAuthProvider oauthProvider;

  private String oauthProviderId;

  public OAuthInfo(OAuthProvider oauthProvider, @NonNull String oauthProviderId) {
    this.oauthProvider = oauthProvider;
    this.oauthProviderId = oauthProviderId;
  }

}
