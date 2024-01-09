package coffeemeet.server.user.domain;

import static coffeemeet.server.user.exception.UserErrorCode.INVALID_PROFILE_IMAGE;

import coffeemeet.server.common.execption.InvalidInputException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthInfo {

  private static final String INVALID_PROFILE_IMAGE_URL_MESSAGE = "올바르지 않은 프로필 사진(%s)입니다.";

  @Enumerated(value = EnumType.STRING)
  private OAuthProvider oauthProvider;

  private String oauthProviderId;

  @Embedded
  private Email email;

  @Column(nullable = false)
  private String profileImageUrl; // TODO: 2024/01/09 해당 필드가 이 클래스에 위치하는게 맞는지?

  public OAuthInfo(@NonNull OAuthProvider oauthProvider, @NonNull String oauthProviderId,
      @NonNull Email email, @NonNull String profileImageUrl) {
    validateProfileImageUrl(profileImageUrl);
    this.oauthProvider = oauthProvider;
    this.oauthProviderId = oauthProviderId;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
  }

  public void updateProfileImageUrl(String newProfileImageUrl) {
    validateProfileImageUrl(newProfileImageUrl);
    this.profileImageUrl = newProfileImageUrl;
  }

  private void validateProfileImageUrl(String profileImageUrl) {
    if (!StringUtils.hasText(profileImageUrl)) {
      throw new InvalidInputException(
          INVALID_PROFILE_IMAGE,
          String.format(INVALID_PROFILE_IMAGE_URL_MESSAGE, profileImageUrl)
      );
    }
  }


}
