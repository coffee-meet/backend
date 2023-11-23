package coffeemeet.server.admin.presentation;

import static coffeemeet.server.common.fixture.entity.AdminFixture.adminLoginHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.reportApprovalHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.reportRejectionHTTPRequest;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.admin.presentation.dto.AdminCustomPage;
import coffeemeet.server.admin.service.AdminService;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.dto.GroupReportListFixture;
import coffeemeet.server.common.fixture.dto.ReportDetailDtoFixture;
import coffeemeet.server.common.fixture.dto.ReportDetailHTTPFixture;
import coffeemeet.server.common.fixture.dto.ReportDtoFixture;
import coffeemeet.server.common.fixture.dto.GroupReportDtoFixture;
import coffeemeet.server.report.presentation.dto.GroupReportList;
import coffeemeet.server.report.presentation.dto.ReportDetailHTTP;
import coffeemeet.server.report.presentation.dto.ReportList;
import coffeemeet.server.report.service.ReportService;
import coffeemeet.server.report.service.dto.ReportDetailDto;
import coffeemeet.server.report.service.dto.ReportDto;
import coffeemeet.server.report.service.dto.ReportDto.Response;
import coffeemeet.server.report.service.dto.GroupReportDto;
import com.epages.restdocs.apispec.Schema;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(AdminController.class)
class AdminControllerTest extends ControllerTestConfig {

  private static final String JSESSION = "JSESSION";
  private static final String SESSION_VALUE = "session";

  private static final String SESSION = "session";

  @MockBean
  private AdminService adminService;

  private String baseUrl = "/api/v1/admins";

  @MockBean
  private ReportService reportService;

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
                        )
                )
                .andExpect(status().isOk());
    }
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
        .andDo(document("report-rejection",
                resourceDetails().tag("관리자").description("관리자 신고 거부")
                    .requestSchema(Schema.schema("ReportRejectionHTTP.Request")),
                requestHeaders(
                    headerWithName("JSESSION").description("세션")
                ),
                requestFields(
                    fieldWithPath("reportId").description("거부할 신고의 ID")
                )
            )
        )
        .andExpect(status().isOk());
  }

    @Test
    @DisplayName("신고 내역을 전체 조회할 수 있다.")
    void findAllReportsTest() throws Exception {
        // given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Response response1 = ReportDtoFixture.reportDto();
        Response response2 = ReportDtoFixture.reportDto();

        List<ReportDto.Response> reportResponses = List.of(response1, response2);

        Page<ReportDto.Response> reportPage = new PageImpl<>(reportResponses, pageable, reportResponses.size());
        List<ReportList> responses = reportResponses.stream()
                .map(ReportList::from)
                .toList();

        AdminCustomPage<ReportList> expectedResponse = new AdminCustomPage<>(responses, reportPage.getTotalElements(), reportPage.getTotalPages());

        given(reportService.findAllReports(any(Pageable.class))).willReturn(reportPage);

        // when, then
        mockMvc.perform(get("/api/v1/admin/reports")
                        .header("JSESSION", SESSION)
                        .sessionAttr("adminId", "admin")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", "createdAt,asc"))
                .andDo(document("find-all-reports",
                        resourceDetails().tag("관리자").description("관리자 신고 내역 전체 조회")
                                .responseSchema(Schema.schema("ReportHTTP.response")),
                        queryParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("size").description("페이지 당 게시물 수"),
                                parameterWithName("sort").description("정렬 기준")
                        ),
                        requestHeaders(
                                headerWithName("JSESSION").description("세션")
                        ),
                        responseFields(
                                fieldWithPath("contents").description("List of report details"),
                                fieldWithPath("contents.[].targetedNickname").type(JsonFieldType.STRING)
                                        .description("신고 대상 닉네임"),
                                fieldWithPath("contents.[].chattingRoomName").type(JsonFieldType.STRING)
                                        .description("신고 대상 채팅방 이름"),
                                fieldWithPath("contents.[].createdAt").type(JsonFieldType.STRING)
                                        .description("신고 생성 날짜"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER)
                                        .description("총 요소의 수"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("동일 채팅방 내의 신고 대상에 대한 신고 내역을 조회할 수 있다.")
    void findReportByTargetIdAndChattingRoomIdTest() throws Exception {
        // given
        List<GroupReportDto.Response> response = List.of(GroupReportDtoFixture.targetReportDto(),
                GroupReportDtoFixture.targetReportDto());
        GroupReportList resultResponse = GroupReportListFixture.groupReportListResponse(response);

        given(reportService.findReportByTargetIdAndChattingRoomId(anyLong(), anyLong())).willReturn(
                response);

        // when, then
        mockMvc.perform(get("/api/v1/admin/reports/group")
                        .header("JSESSION", SESSION)
                        .sessionAttr("adminId", "admin")
                        .contentType(APPLICATION_JSON)
                        .param("targetedId", String.valueOf(1L))
                        .param("chattingRoomId", String.valueOf(1L)))
                .andDo(document("find-reports-by-targetedId-and-chattingRoomId",
                        resourceDetails().tag("관리자").description("신고 대상과 채팅방 아이디로 관리자 신고 내역 조회")
                                .responseSchema(Schema.schema("TargetReportHTTP.Response")),
                        queryParameters(
                                parameterWithName("targetedId").description("신고 대상 아이디"),
                                parameterWithName("chattingRoomId").description("신고 대상 채팅방 아이디")
                        ),
                        requestHeaders(
                                headerWithName("JSESSION").description("세션")
                        ),
                        responseFields(
                                fieldWithPath("reports.[].reporterNickname").type(JsonFieldType.STRING)
                                        .description("신고자 닉네임"),
                                fieldWithPath("reports.[].createdAt").type(JsonFieldType.STRING).description("신고 생성 날짜")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(resultResponse)));
    }

    @Test
    @DisplayName("신고 아이디로 신고 내역을 조회할 수 있다.")
    void findReportTest() throws Exception {
        // given
        Long reportId = 1L;
        ReportDetailDto.Response response = ReportDetailDtoFixture.reportDetailDto();
        ReportDetailHTTP.Response expectedResponse = ReportDetailHTTPFixture.reportDetailHTTPResponse(
                response);

        given(reportService.findReportById(anyLong())).willReturn(response);

        // when, then
        mockMvc.perform(get("/api/v1/admin/reports/detail/{reportId}", reportId)
                        .header("JSESSION", SESSION)
                        .sessionAttr("adminId", "admin")
                        .contentType(APPLICATION_JSON))
                .andDo(document("find-report-by-id",
                                resourceDetails().tag("관리자").description("관리자 신고 내역 조회")
                                        .responseSchema(Schema.schema("ReportHTTP.Response")),
                                pathParameters(
                                        parameterWithName("reportId").description("신고 아이디")
                                ),
                                requestHeaders(
                                        headerWithName("JSESSION").description("세션")
                                ),
                                responseFields(
                                        fieldWithPath("reporterNickname").type(JsonFieldType.STRING).description("신고자 닉네임"),
                                        fieldWithPath("targetedNickname").type(JsonFieldType.STRING)
                                                .description("신고 대상 닉네임"),
                                        fieldWithPath("targetedEmail").type(JsonFieldType.STRING).description("신고 대상 이메일"),
                                        fieldWithPath("reason").type(JsonFieldType.STRING).description("신고 사유"),
                                        fieldWithPath("reasonDetail").type(JsonFieldType.STRING).description("신고 상세 사유"),
                                        fieldWithPath("reportedCount").type(JsonFieldType.NUMBER).description("신고 누적 횟수"),
                                        fieldWithPath("createAt").type(JsonFieldType.STRING).description("신고 생성 날짜")
                                )
                        )
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
    }

}
