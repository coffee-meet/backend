package coffeemeet.server.user.domain;

import static coffeemeet.server.user.exception.UserErrorCode.INVALID_NAME;
import static coffeemeet.server.user.exception.UserErrorCode.INVALID_NICKNAME;
import static coffeemeet.server.user.exception.UserErrorCode.INVALID_PROFILE_IMAGE;

import coffeemeet.server.common.execption.DataLengthExceededException;
import coffeemeet.server.common.execption.InvalidInputException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
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
  private static final String INVALID_NAME_MESSAGE = "올바르지 않은 이름(%s)입니다.";
  private static final String INVALID_PROFILE_IMAGE_URL_MESSAGE = "올바르지 않은 프로필 사진(%s)입니다.";

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, length = NICKNAME_MAX_LENGTH)
  private String nickname;

  @Embedded
  @Column(nullable = false)
  private Email email;

  @Column(nullable = false)
  private String profileImageUrl;

  @Embedded
  @Column(nullable = false)
  private Birth birth;

  @Builder
  private Profile(
      @NonNull String name,
      @NonNull String nickname,
      @NonNull Email email,
      @NonNull String profileImageUrl,
      @NonNull Birth birth
  ) {
    validateNickname(nickname);
    validateName(name);
    validateProfileImageUrl(profileImageUrl);
    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
    this.birth = birth;
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
      throw new DataLengthExceededException(INVALID_NICKNAME, INVALID_NICKNAME_MESSAGE);
    }
  }

  private void validateName(String name) {
    if (!StringUtils.hasText(name)) {
      throw new InvalidInputException(INVALID_NAME, INVALID_NAME_MESSAGE);
    }
  }

  private void validateProfileImageUrl(String profileImageUrl) {
    if (!StringUtils.hasText(profileImageUrl)) {
      throw new InvalidInputException(INVALID_PROFILE_IMAGE, INVALID_PROFILE_IMAGE_URL_MESSAGE);
    }
  }

}
