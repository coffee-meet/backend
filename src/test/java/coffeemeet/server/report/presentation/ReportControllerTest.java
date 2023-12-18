package coffeemeet.server.report.presentation;

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
import coffeemeet.server.report.presentation.dto.CreateReportHTTP;
import coffeemeet.server.report.service.ReportService;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(ReportController.class)
class ReportControllerTest extends ControllerTestConfig {

  @MockBean
  private ReportService reportService;

  @BeforeEach
  void setUp() {
    RefreshToken refreshToken = AuthFixture.refreshToken();
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
  }

  @DisplayName("신고를 생성할 수 있다.")
  @Test
  void reportUser() throws Exception {
    // given
    Long reporterId = 1L;
    Long targetId = 1L;
    Long chattingRoomId = 1L;
    CreateReportHTTP.Request request = new CreateReportHTTP.Request(chattingRoomId, targetId, "잠수",
        "매칭 시작해놓고 잠수타요.");

    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(reporterId);
    willDoNothing().given(reportService).reportUser(anyLong(), anyLong(), anyLong(), any(), any());

    // when, then
    mockMvc.perform(post("/api/v1/reports")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andDo(document("user-report",
            resourceDetails().tag("신고").description("사용자 신고")
                .requestSchema(Schema.schema("ReportHTTP.Request")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("토큰")),
            requestFields(
                fieldWithPath("chattingRoomId").type(JsonFieldType.NUMBER).description("채팅방 아이디"),
                fieldWithPath("targetedId").type(JsonFieldType.NUMBER).description("신고 대상 아이디"),
                fieldWithPath("reason").type(JsonFieldType.STRING).description("신고 사유"),
                fieldWithPath("reasonDetail").type(JsonFieldType.STRING).description("신고 상세 사유")
            )
        ))
        .andExpect(status().isOk());
  }

}
