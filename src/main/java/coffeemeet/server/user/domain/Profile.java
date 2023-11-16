package coffeemeet.server.user.domain;

import static coffeemeet.server.user.exception.UserErrorCode.INVALID_NICKNAME;
import static coffeemeet.server.user.exception.UserErrorCode.INVALID_PROFILE_IMAGE;

import coffeemeet.server.common.execption.DataLengthExceededException;
import coffeemeet.server.common.execption.InvalidInputException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

  private static final int NICKNAME_MAX_LENGTH = 20;
  private static final String INVALID_NICKNAME_MESSAGE = "올바르지 않은 닉네임(%s)입니다.";
  private static final String INVALID_PROFILE_IMAGE_URL_MESSAGE = "올바르지 않은 프로필 사진(%s)입니다.";

  @Column(nullable = false, length = NICKNAME_MAX_LENGTH)
  private String nickname;

  @Embedded
  private Email email;

  @Column(nullable = false)
  private String profileImageUrl;

  public Profile(
      @NonNull String nickname,
      @NonNull Email email,
      @NonNull String profileImageUrl
  ) {
    validateNickname(nickname);
    validateProfileImageUrl(profileImageUrl);
    this.nickname = nickname;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
  }

  public void updateNickname(String newNickname) {
    validateNickname(nickname);
    this.nickname = newNickname;
  }

  public void updateProfileImageUrl(String newProfileImageUrl) {
    validateProfileImageUrl(newProfileImageUrl);
    this.profileImageUrl = newProfileImageUrl;
  }

  private void validateNickname(String nickname) {
    if (!StringUtils.hasText(nickname) || nickname.length() > NICKNAME_MAX_LENGTH) {
      throw new DataLengthExceededException(
          INVALID_NICKNAME,
          String.format(INVALID_NICKNAME_MESSAGE, nickname)
      );
    }
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
