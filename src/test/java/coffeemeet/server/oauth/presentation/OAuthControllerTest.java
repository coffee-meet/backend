package coffeemeet.server.oauth.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.AuthFixture;
import coffeemeet.server.oauth.service.OAuthService;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(OAuthController.class)
class OAuthControllerTest extends ControllerTestConfig {

  @MockBean
  private OAuthService oAuthService;

  @MockBean
  private UserService userService;

  @DisplayName("sns 접근 권한 url로 redirect 할 수 있다.")
  @Test
  void redirectAuthCodeRequestUrlTest() throws Exception {
    // given
    String expectedRedirectUrl = "https://example.com";

    given(oAuthService.getAuthCodeRequestUrl(OAuthProvider.KAKAO)).willReturn(
        expectedRedirectUrl);

    // when, then
    mockMvc.perform(get("/api/v1/oauth2.0/{oAuthProvider}", OAuthProvider.KAKAO)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("oauth-redirect",
                resourceDetails().tag("인증").description("접근 권한 url 리다이렉트"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("oAuthProvider").description("OAuth 로그인 타입")
                ),
                responseHeaders(
                    headerWithName("Location").description("리다이렉션 대상 URL")
                )
            )
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(expectedRedirectUrl));
  }

  @DisplayName("사용자는 회원탈퇴 할 수 있다.")
  @Test
  void deleteTest() throws Exception {
    // given
    RefreshToken refreshToken = AuthFixture.refreshToken();
    willDoNothing().given(userService).deleteUser(anyLong(), anyString(), any());
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);

    // when, then
    mockMvc.perform(post("/api/v1/oauth2.0/delete")
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
