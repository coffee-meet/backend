package coffeemeet.server.auth.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.auth.service.AuthService;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.dto.AuthTokensFixture;
import coffeemeet.server.common.fixture.dto.RefreshTokenFixture;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTestConfig {

  @MockBean
  private AuthService authService;

  @DisplayName("refresh token 을 통해 access token 을 갱신할 수 있다.")
  @Test
  void renewTest() throws Exception {
    // given
    AuthTokens authTokens = AuthTokensFixture.authTokens();
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();

    given(authService.renew(anyLong(), any())).willReturn(authTokens);
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);

    // when, then
    mockMvc.perform(post("/api/v1/auth/renew-token")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("auth-renew",
                resourceDetails().tag("인증").description("토큰 갱신")
                    .responseSchema(Schema.schema("AuthTokens")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                ),
                responseFields(
                    fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                    fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                )
            )
        )
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(authTokens)));
  }

  @DisplayName("사용자는 로그아웃 할 수 있다.")
  @Test
  void logoutTest() throws Exception {
    // given
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();
    willDoNothing().given(authService).logout(anyLong());
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);

    // when, then
    mockMvc.perform(post("/api/v1/auth/logout")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("auth-logout",
                resourceDetails().tag("인증").description("로그아웃"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                )
            )
        )
        .andExpect(status().isOk());
  }

  @DisplayName("사용자는 회원탈퇴 할 수 있다.")
  @Test
  void deleteTest() throws Exception {
    // given
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();
    willDoNothing().given(authService).delete(anyLong());
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);

    // when, then
    mockMvc.perform(post("/api/v1/auth/delete")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("auth-delete",
                resourceDetails().tag("인증").description("회원탈퇴"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                )
            )
        )
        .andExpect(status().isOk());
  }

}
