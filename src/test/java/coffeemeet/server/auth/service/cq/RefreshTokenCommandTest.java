package coffeemeet.server.auth.service.cq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.auth.repository.RefreshTokenRepository;
import coffeemeet.server.common.fixture.dto.RefreshTokenFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenCommandTest {

  @InjectMocks
  private RefreshTokenCommand refreshTokenCommand;

  @Mock
  private RefreshTokenRepository refreshTokenRepository;

  @DisplayName("리프레시 토큰을 생성할 수 있다.")
  @Test
  void createRefreshTokenTest() {
    // given
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();

    // when
    refreshTokenCommand.createRefreshToken(refreshToken);

    // then
    then(refreshTokenRepository).should(only()).save(any(RefreshToken.class));
  }

  @DisplayName("리프레시 토큰을 삭제할 수 있다.")
  @Test
  void deleteRefreshTokenTest() {
    // given, when
    Long userId = 1L;
    refreshTokenCommand.deleteRefreshToken(userId);

    // then
    then(refreshTokenRepository).should(only()).deleteById(anyLong());
  }

}
