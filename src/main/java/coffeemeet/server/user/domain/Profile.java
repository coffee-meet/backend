package coffeemeet.server.user.domain;

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
      throw new IllegalArgumentException("올바르지 않은 닉네임입니다.");
    }
  }

  private void validateName(String name) {
    if (!StringUtils.hasText(name)) {
      throw new IllegalArgumentException("올바르지 않은 이름입니다.");
    }
  }

  private void validateProfileImageUrl(String profileImageUrl) {
    if (!StringUtils.hasText(profileImageUrl)) {
      throw new IllegalArgumentException("올바르지 않은 프로필 이미지 url입니다.");
    }
  }

}
