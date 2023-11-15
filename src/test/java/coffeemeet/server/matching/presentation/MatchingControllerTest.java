package coffeemeet.server.matching.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.dto.RefreshTokenFixture;
import coffeemeet.server.matching.service.MatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(MatchingController.class)
class MatchingControllerTest extends ControllerTestConfig {

  @MockBean
  private MatchingService matchingService;

  @BeforeEach
  void setUp() {
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
  }

  @Test
  @DisplayName("매칭을 시작할 수 있다.")
  void startTest() throws Exception {
    // given
    long userId = 1;
    willDoNothing().given(matchingService).start(userId);

    // when, then
    mockMvc.perform(post("/api/v1/matching/start")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("matching-start",
            resourceDetails().tag("매칭").description("매칭 시작"))
        )
        .andExpect(status().isOk());
  }

}
