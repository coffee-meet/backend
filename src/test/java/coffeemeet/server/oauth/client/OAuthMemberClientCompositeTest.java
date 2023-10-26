package coffeemeet.server.oauth.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import coffeemeet.server.common.fixture.dto.OAuthInfoResponseFixture;
import coffeemeet.server.oauth.dto.OAuthInfoResponse;
import coffeemeet.server.user.domain.OAuthProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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

  @Test
  void fetchTest() {
    // given
    String authCode = "authCode";
    OAuthProvider oAuthProvider = OAuthProvider.KAKAO;
    OAuthInfoResponse response = OAuthInfoResponseFixture.oAuthInfoResponse();

    // when
    when(client.oAuthProvider()).thenReturn(OAuthProvider.KAKAO);
    when(client.fetch(authCode)).thenReturn(response);

    clients = new HashSet<>(Collections.singletonList(client));
    composite = new OAuthMemberClientComposite(clients);

    // then
    OAuthInfoResponse expectedResponse = composite.fetch(oAuthProvider, authCode);
    assertThat(response).isEqualTo(expectedResponse);
  }

}
