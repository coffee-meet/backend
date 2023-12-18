package coffeemeet.server.matching.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
  @DisplayName("매칭 시작 요청을 처리할 수 있다.")
  void startTest() throws Exception {
    // given
    Long userId = 1L;
    willDoNothing().given(matchingService).startMatching(userId);

    // when, then
    mockMvc.perform(post("/api/v1/matching/start")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("matching-start",
                resourceDetails().tag("매칭").description("매칭 시작"),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                )
            )
        )
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("매칭 취소 요청을 처리할 수 있다.")
  void cancelTest() throws Exception {
    // given
    Long userId = 1L;
    willDoNothing().given(matchingService).cancelMatching(userId);

    // when, then
    mockMvc.perform(post("/api/v1/matching/cancel")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("matching-cancel",
                resourceDetails().tag("매칭").description("매칭 취소"),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                )
            )
        )
        .andExpect(status().isOk());
  }

}
