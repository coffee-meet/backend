package coffeemeet.server.admin.presentation;

import static coffeemeet.server.common.fixture.entity.AdminFixture.adminLoginHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.reportApprovalHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.reportRejectionHTTPRequest;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.admin.service.AdminService;
import coffeemeet.server.common.config.ControllerTestConfig;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(AdminController.class)
class AdminControllerTest extends ControllerTestConfig {

  private static final String JSESSION = "JSESSION";
  private static final String SESSION_VALUE = "session";


  @MockBean
  private AdminService adminService;

  private String baseUrl = "/api/v1/admins";

  @Test
  @DisplayName("관리자 로그인을 할 수 있다.")
  void loginTest() throws Exception {
    // given
    String loginRequest = objectMapper.writeValueAsString(adminLoginHTTPRequest());

    // when, then
    mockMvc.perform(post(baseUrl + "/login")
            .contentType(APPLICATION_JSON)
            .content(loginRequest))
        .andDo(document("admin-login",
            resourceDetails().tag("관리자").description("관리자 로그인")
                .requestSchema(Schema.schema("AdminLoginHTTP.Request")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("id").description("관리자 ID"),
                fieldWithPath("password").description("관리자 비밀번호")
            )
        ))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("관리자 로그아웃 할 수 있다.")
  void name() throws Exception {
    // given, when, then
    mockMvc.perform(post(baseUrl + "/logout")
            .header(JSESSION, SESSION_VALUE)
        )
        .andDo(document("admin-logout",
            resourceDetails().tag("관리자").description("관리자 로그아웃"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName(JSESSION).description("세션")
            )
        ))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("인증 허가 요청을 처리할 수 있다.")
  void approveCertification() throws Exception {
    // given
    Long certificationId = 1L;

    // when, then
    mockMvc.perform(patch(baseUrl + "/certifications/{certificationId}/approval", certificationId)
            .header(JSESSION, SESSION_VALUE)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON)
        )
        .andDo(document(
                "certification-approval",
                resourceDetails()
                    .tag("관리자")
                    .description("회사 인증 허가"),
                requestHeaders(
                    headerWithName(JSESSION).description("세션")
                )
            )
        )
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("인증 반려 요청을 처리할 수 있다.")
  void rejectCertification() throws Exception {
    // given
    Long certificationId = 1L;

    // when, then
    mockMvc.perform(delete(baseUrl + "/certifications/{certificationId}/rejection", certificationId)
            .header(JSESSION, SESSION_VALUE)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON)
        )
        .andDo(document(
                "certification-rejection",
                resourceDetails()
                    .tag("관리자")
                    .description("회사 인증 반려"),
                requestHeaders(
                    headerWithName(JSESSION).description("세션")
                )
            )
        )
        .andExpect(status().isOk());
  }


  @Test
  @DisplayName("신고 승인 요청을 처리할 수 있다.")
  void assignReportPenalty() throws Exception {
    // given
    Long userId = 1L;
    String approvalRequestJson = objectMapper.writeValueAsString(reportApprovalHTTPRequest());

    // when, then
    mockMvc.perform(patch(baseUrl + "/users/{userId}/punishment", userId)
            .header(JSESSION, SESSION_VALUE)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON)
            .content(approvalRequestJson)
        )
        .andDo(document("user-punishment",
            resourceDetails()
                .tag("관리자")
                .description("사용자 처벌 및 신고 처리")
                .requestSchema(Schema.schema("UserPunishmentHTTP.Request")),
            requestHeaders(
                headerWithName(JSESSION).description("세션")
            ),
            requestFields(
                fieldWithPath("reportIds").description("특정 채팅방에서 특정 사용자에게 들어온 신고 ID 목록")
            )
        ))
        .andExpect(status().isOk());
  }


  @Test
  @DisplayName("신고 거부 요청을 처리할 수 있다.")
  void dismissReport() throws Exception {
    // given
    String rejectionRequestJson = objectMapper.writeValueAsString(reportRejectionHTTPRequest());

    // when, then
    mockMvc.perform(delete(baseUrl + "/reports")
            .header(JSESSION, SESSION_VALUE)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON)
            .content(rejectionRequestJson))
        .andDo(document("report-delete",
            resourceDetails().tag("관리자").description("신고 무시 및 삭제")
                .requestSchema(Schema.schema("ReportDeletionHTTP.Request")),
            requestHeaders(
                headerWithName(JSESSION).description("세션")
            ),
            requestFields(
                fieldWithPath("reportIds").description("삭제할 신고 ID 목록")
            )
        ))
        .andExpect(status().isOk());
  }

}
