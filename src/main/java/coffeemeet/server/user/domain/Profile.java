package coffeemeet.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
      String name,
      String nickname,
      Email email,
      String profileImageUrl,
      Birth birth
  ) {
    validateName(name);
    validateNickname(nickname);
    validateProfileImage(profileImageUrl);
    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
    this.birth = birth;
  }

  private void validateName(String name) {
    if (!StringUtils.hasText(name)) {
      throw new IllegalArgumentException("올바르지 않은 이름입니다.");
    }
  }

  private void validateNickname(String nickname) {
    if (!StringUtils.hasText(nickname) || nickname.length() > NICKNAME_MAX_LENGTH) {
      throw new IllegalArgumentException("올바르지 않은 닉네임입니다.");
    }
  }

  private void validateProfileImage(String profileImageUrl) {
    if (!StringUtils.hasText(profileImageUrl)) {
      throw new IllegalArgumentException("올바르지 않은 프로필 사진 url입니다.");
    }
  }

}
