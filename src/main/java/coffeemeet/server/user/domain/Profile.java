package coffeemeet.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@NoArgsConstructor
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

  @Column(nullable = false)
  private String birth;

  @Column(nullable = false)
  private String department;

  @Builder
  private Profile(
      String name,
      String nickname,
      Email email,
      String profileImageUrl,
      String birth,
      String department
  ) {
    validateName(name);
    validateNickname(nickname);
    validateProfileImage(profileImageUrl);
    validateBirth(birth);
    validateDepartment(department);
    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
    this.birth = birth;
    this.department = department;
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

  private void validateBirth(String birth) {
    if (!StringUtils.hasText(birth)) {
      throw new IllegalArgumentException("올바르지 않은 생년월일입니다.");
    }
  }

  private void validateDepartment(String department) {
    if (!StringUtils.hasText(department)) {
      throw new IllegalArgumentException("올바르지 않은 부서 이름입니다.");
    }
  }

}
