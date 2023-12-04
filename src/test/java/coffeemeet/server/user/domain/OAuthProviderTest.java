package coffeemeet.server.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OAuthProviderTest {

  @Test
  @DisplayName("입력받은 로그인 타입 문자열에 대한 로그인 타입을 반환할 수 있다.")
  void fromTest() {
    // given
    String kakao = "kakao";

    // when
    OAuthProvider oAuthProvider = OAuthProvider.from(kakao);

    // then
    assertThat(oAuthProvider).isEqualTo(OAuthProvider.KAKAO);
  }

  @Test
  @DisplayName("로그인 타입들을 가져올 수 있다.")
  void valuesTest() {
    // when
    OAuthProvider[] oAuthProviders = OAuthProvider.values();

    // then
    assertThat(oAuthProviders).hasSize(2);
    assertThat(oAuthProviders).contains(OAuthProvider.KAKAO, OAuthProvider.NAVER);
  }

  @Test
  @DisplayName("해당 문자열에 대한 로그인 타입을 가져올 수 있다.")
  void valueOfTest() {
    // when
    OAuthProvider oAuthProvider = OAuthProvider.valueOf("NAVER");

    // then
    assertThat(oAuthProvider).isEqualTo(OAuthProvider.NAVER);
  }

}
