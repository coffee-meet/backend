package coffeemeet.server.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import coffeemeet.server.common.execption.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OAuthInfoTest {

  @Test
  @DisplayName("로그인 정보를 생성할 수 있다.")
  void oAuthInfoTest() {
    // when, then
    assertThatCode(OAuthInfo::new).doesNotThrowAnyException();
    assertThatCode(() ->
        new OAuthInfo(OAuthProvider.KAKAO, "123", new Email("test123@gmail.com"), "image"))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("로그인 정보를 생성시 입력받는 정보가 null일 경우 예외를 던진다.")
  void oAuthInfo_NullPointerExceptionTest() {
    // when, then
    assertAll(
        () -> assertThatThrownBy(() ->
            new OAuthInfo(null, "123", new Email("test123@gmail.com"), "image"))
            .isInstanceOf(NullPointerException.class),
        () -> assertThatThrownBy(() ->
            new OAuthInfo(OAuthProvider.KAKAO, null, new Email("test123@gmail.com"), "image"))
            .isInstanceOf(NullPointerException.class),
        () -> assertThatThrownBy(() ->
            new OAuthInfo(OAuthProvider.KAKAO, "123", null, "image"))
            .isInstanceOf(NullPointerException.class),
        () -> assertThatThrownBy(() ->
            new OAuthInfo(OAuthProvider.KAKAO, "123", new Email("test123@gmail.com"), null))
            .isInstanceOf(NullPointerException.class)
    );
  }

  @Test
  @DisplayName("프로필 이미지를 변경할 수 있다.")
  void updateProfileImageUrlTest() {
    // given
    OAuthInfo oAuthInfo = new OAuthInfo();
    String beforeProfileImage = oAuthInfo.getProfileImageUrl();
    String afterProfileImage = "after";

    // when
    oAuthInfo.updateProfileImageUrl(afterProfileImage);

    // then
    assertThat(oAuthInfo.getProfileImageUrl()).isNotEqualTo(beforeProfileImage);
    assertThat(oAuthInfo.getProfileImageUrl()).isEqualTo(afterProfileImage);
  }

  @Test
  @DisplayName("올바르지 않은 프로필 이미지일 경우 예외를 던진다.")
  void updateProfileImageUrl_InvalidInputExceptionTest() {
    // given
    OAuthInfo oAuthInfo = new OAuthInfo();
    String afterProfileImage = " ";

    // when, then
    assertThatThrownBy(() -> oAuthInfo.updateProfileImageUrl(afterProfileImage))
        .isInstanceOf(InvalidInputException.class);
  }

}
