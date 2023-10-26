package coffeemeet.server.oauth.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.common.fixture.dto.OAuthInfoResponseFixture;
import coffeemeet.server.oauth.dto.OAuthInfoDto;
import coffeemeet.server.user.domain.OAuthProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OAuthMemberClientCompositeTest {

  @Mock
  private OAuthMemberClient client;

  @Mock
  private Set<OAuthMemberClient> clients;

  @InjectMocks
  private OAuthMemberClientComposite composite;

  @DisplayName("sns 로부터 사용자 정보를 가져올 수 있다.")
  @Test
  void fetchTest() {
    // given
    String authCode = "authCode";
    OAuthProvider oAuthProvider = OAuthProvider.KAKAO;
    OAuthInfoDto.Response response = OAuthInfoResponseFixture.oAuthInfoResponse();

    given(client.oAuthProvider()).willReturn(OAuthProvider.KAKAO);
    given(client.fetch(authCode)).willReturn(response);

    clients = new HashSet<>(Collections.singletonList(client));
    composite = new OAuthMemberClientComposite(clients);

    // when
    OAuthInfoDto.Response expectedResponse = composite.fetch(oAuthProvider, authCode);

    // then
    assertThat(response).isEqualTo(expectedResponse);
  }

}
