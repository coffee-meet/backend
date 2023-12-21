package coffeemeet.server.inquiry.presentation;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.AuthFixture;
import coffeemeet.server.common.fixture.InquiryFixture;
import coffeemeet.server.inquiry.presentation.dto.InquiryHTTP;
import coffeemeet.server.inquiry.service.InquiryService;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(InquiryController.class)
class InquiryControllerTest extends ControllerTestConfig {

  @MockBean
  private InquiryService inquiryService;

  @DisplayName("문의를 할 수 있다.")
  @Test
  void inquireTest() throws Exception {
    // given
    Long inquirerId = 1L;
    InquiryHTTP.Request request = InquiryFixture.request();
    RefreshToken refreshToken = AuthFixture.refreshToken();

    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(inquirerId);
    willDoNothing().given(inquiryService).registerInquiry(anyLong(), any(), any());

    // when, then
    mockMvc.perform(post("/api/v1/inquiries")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andDo(document("user-inquiry",
            resourceDetails().tag("문의").description("사용자 문의")
                .requestSchema(Schema.schema(" InquiryHTTP.Request")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("토큰")),
            requestFields(
                fieldWithPath("title").type(JsonFieldType.STRING).description("문의 제목"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("문의 내용")
            )
        ))
        .andExpect(status().isOk());
  }

}
