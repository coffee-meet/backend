package coffeemeet.server.auth.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.auth.infrastructure.RefreshTokenRepository;
import coffeemeet.server.common.fixture.dto.RefreshTokenFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenQueryTest {

  @InjectMocks
  private RefreshTokenQuery refreshTokenQuery;

  @Mock
  private RefreshTokenRepository refreshTokenRepository;

  @DisplayName("리프레시 토큰을 조회할 수 있다.")
  @Test
  void getRefreshTokenTest() {
    // given
    Long userId = 1L;
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();
    given(refreshTokenRepository.findById(userId)).willReturn(Optional.of(refreshToken));

    // when
    RefreshToken foundRefreshToken = refreshTokenQuery.getRefreshToken(userId);

    // then
    assertThat(foundRefreshToken).isEqualTo(refreshToken);
  }

}
