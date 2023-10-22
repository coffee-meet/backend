package coffeemeet.server.auth.controller;

import coffeemeet.server.auth.common.config.ControllerTestConfig;
import coffeemeet.server.auth.service.AuthService;
import coffeemeet.server.user.domain.OAuthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTestConfig {

  @MockBean
  private AuthService authService;

  @DisplayName("사용자가 로그인 버튼을 누르면 해당하는 서비스의 접근 권한 url로 리다이렉트한다.")
  @Test
  void redirectAuthCodeRequestUrlTest() throws Exception {
    String expectedRedirectUrl = "https://example.com";

    when(authService.getAuthCodeRequestUrl(eq(OAuthProvider.KAKAO))).thenReturn(
        expectedRedirectUrl);

    mockMvc.perform(get("/oauth2/auth/{oAuthProvider}", OAuthProvider.KAKAO)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("auth-redirect",
                resourceDetails().tag("Auth").description("접근 권한 url 리다이렉트"),
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

}
