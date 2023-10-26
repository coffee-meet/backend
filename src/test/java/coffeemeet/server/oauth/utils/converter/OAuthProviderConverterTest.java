package coffeemeet.server.oauth.utils.converter;

import static org.assertj.core.api.Assertions.assertThat;

import coffeemeet.server.user.domain.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class OAuthProviderConverterTest {

  private OAuthProviderConverter oAuthProviderConverter = new OAuthProviderConverter();

  @DisplayName("입력된 sns provider 이름을 항상 대문자로 변환 시킬 수 있다.")
  @Test
  void convert() {
    // given
    String provider = "kakao";

    // when
    OAuthProvider oAuthProvider = oAuthProviderConverter.convert(provider);

    // then
    assertThat(oAuthProvider).isEqualTo(OAuthProvider.KAKAO);
  }

}
