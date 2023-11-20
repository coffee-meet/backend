package coffeemeet.server.admin.presentation;

import static coffeemeet.server.common.fixture.entity.AdminFixture.adminLoginHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.certificationApprovalHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.certificationRejectionHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.reportApprovalHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.reportRejectionHTTPRequest;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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

  private static final String SESSION = "session";
  @MockBean
  private AdminService adminService;

  @Test
  @DisplayName("관리자 로그인을 할 수 있다.")
  void loginTest() throws Exception {
    // given
    String loginRequest = objectMapper.writeValueAsString(adminLoginHTTPRequest());

    // when, then
    mockMvc.perform(post("/api/v1/admin/login")
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
    mockMvc.perform(post("/api/v1/admin/logout")
            .header("JSESSION", SESSION)
        )
        .andDo(document("admin-logout",
            resourceDetails().tag("관리자").description("관리자 로그아웃")
                .requestSchema(Schema.schema("CertificationApprovalHTTP.Request")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("JSESSION").description("세션")
            )
        ))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("인증 허가 요청을 처리할 수 있다.")
  void approveCertification() throws Exception {
    // given
    String approvalRequest = objectMapper.writeValueAsString(certificationApprovalHTTPRequest());
    // when, then
    mockMvc.perform(patch("/api/v1/admin/certification/approval")
            .header("JSESSION", SESSION)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON)
            .content(approvalRequest))
        .andDo(document("certification-approval",
            resourceDetails().tag("관리자").description("관리자 인증 허가")
                .requestSchema(Schema.schema("CertificationApprovalHTTP.Request")),
            requestHeaders(
                headerWithName("JSESSION").description("세션")
            ),
            requestFields(
                fieldWithPath("userId").description("승인할 사용자의 ID")
            )
        ))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("인증 반려 요청을 처리할 수 있다.")
  void rejectCertification() throws Exception {
    // given
    String rejectionRequest = objectMapper.writeValueAsString(certificationRejectionHTTPRequest());

    // when, then
    mockMvc.perform(patch("/api/v1/admin/certification/rejection")
            .header("JSESSION", SESSION)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON)
            .content(rejectionRequest))
        .andDo(document("certification-rejection",
            resourceDetails().tag("관리자").description("관리자 인증 반려")
                .requestSchema(Schema.schema("CertificationRejectionHTTP.Request")),
            requestHeaders(
                headerWithName("JSESSION").description("세션")
            ),
            requestFields(
                fieldWithPath("userId").description("반려할 사용자의 ID")
            )
        ))
        .andExpect(status().isOk());
  }


  @Test
  @DisplayName("신고 승인 요청을 처리할 수 있다.")
  void assignReportPenalty() throws Exception {
    // given
    String approvalRequestJson = objectMapper.writeValueAsString(reportApprovalHTTPRequest());

    // when, then
    mockMvc.perform(patch("/api/v1/admin/report/punishment")
            .header("JSESSION", SESSION)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON)
            .content(approvalRequestJson))
        .andDo(document("report-punishment",
            resourceDetails().tag("관리자").description("관리자 신고 승인")
                .requestSchema(Schema.schema("ReportApprovalHTTP.Request")),
            requestHeaders(
                headerWithName("JSESSION").description("세션")
            ),
            requestFields(
                fieldWithPath("reportId").description("승인할 신고의 ID"),
                fieldWithPath("userId").description("신고된 사용자의 ID")
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
    mockMvc.perform(patch("/api/v1/admin/report/rejection")
            .header("JSESSION", SESSION)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON)
            .content(rejectionRequestJson))
        .andDo(document("report-rejection",
            resourceDetails().tag("관리자").description("관리자 신고 거부")
                .requestSchema(Schema.schema("ReportRejectionHTTP.Request")),
            requestHeaders(
                headerWithName("JSESSION").description("세션")
            ),
            requestFields(
                fieldWithPath("reportId").description("거부할 신고의 ID")
            )
        ))
        .andExpect(status().isOk());
  }

}
