package coffeemeet.server.admin.presentation;

import static coffeemeet.server.common.fixture.entity.AdminFixture.adminLoginHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.reportApprovalHTTPRequest;
import static coffeemeet.server.common.fixture.entity.AdminFixture.reportRejectionHTTPRequest;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.pageable;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.pendingCertificationPageDto;
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
import coffeemeet.server.admin.presentation.dto.AdminCustomSlice;
import coffeemeet.server.admin.service.AdminService;
import coffeemeet.server.certification.service.CertificationService;
import coffeemeet.server.certification.service.dto.PendingCertification;
import coffeemeet.server.certification.service.dto.PendingCertificationPageDto;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.dto.GroupReportDtoFixture;
import coffeemeet.server.common.fixture.dto.GroupReportListFixture;
import coffeemeet.server.common.fixture.dto.ReportDetailDtoFixture;
import coffeemeet.server.common.fixture.dto.ReportDetailHTTPFixture;
import coffeemeet.server.common.fixture.dto.ReportDtoFixture;
import coffeemeet.server.common.fixture.entity.AdminFixture;
import coffeemeet.server.common.fixture.entity.InquiryFixture;
import coffeemeet.server.inquiry.presentation.dto.InquiryDetailHTTP;
import coffeemeet.server.inquiry.service.InquiryService;
import coffeemeet.server.inquiry.service.dto.InquiryDetailDto;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse.InquirySummary;
import coffeemeet.server.report.presentation.dto.GroupReportList;
import coffeemeet.server.report.presentation.dto.ReportDetailHTTP;
import coffeemeet.server.report.presentation.dto.ReportList;
import coffeemeet.server.report.service.ReportService;
import coffeemeet.server.report.service.dto.GroupReportDto;
import coffeemeet.server.report.service.dto.ReportDetailDto;
import coffeemeet.server.report.service.dto.ReportDto;
import com.epages.restdocs.apispec.Schema;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(AdminController.class)
class AdminControllerTest extends ControllerTestConfig {

  private static final String JSESSION = "JSESSION";
  private static final String SESSION_VALUE = "session";
  private final String baseUrl = "/api/v1/admins";

  @MockBean
  private AdminService adminService;

  @MockBean
  private InquiryService inquiryService;

  @MockBean
  private ReportService reportService;

  @MockBean
  private CertificationService certificationService;

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
                resourceDetails().tag("관리자").description("회사 인증 허가"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
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
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
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
            resourceDetails().tag("관리자").description("사용자 처벌 및 신고 처리")
                .requestSchema(Schema.schema("UserPunishmentHTTP.Request")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
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
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName(JSESSION).description("세션")
            ),
            requestFields(
                fieldWithPath("reportIds").description("삭제할 신고 ID 목록")
            )
        ))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("신고 내역을 전체 조회할 수 있다.")
  void findAllReportsTest() throws Exception {
    // given
    long lastReportId = 0L;
    int pageSize = 10;
    ReportDto response1 = ReportDtoFixture.reportDto();
    ReportDto response2 = ReportDtoFixture.reportDto();

    List<ReportDto> reportResponses = List.of(response1, response2);
    boolean hasNext = true;

    ReportList reportList = ReportList.of(reportResponses, hasNext);

    AdminCustomPage<ReportDto> result = new AdminCustomPage<>(reportList.contents(),
        reportList.hasNext());

    given(reportService.findAllReports(lastReportId, pageSize)).willReturn(reportList);

    // when, then
    mockMvc.perform(get("/api/v1/admins/reports")
            .header("JSESSION", SESSION_VALUE)
            .sessionAttr("adminId", "admin")
            .param("lastReportId", String.valueOf(lastReportId))
            .param("pageSize", String.valueOf(pageSize)))
        .andDo(document("find-all-reports",
            resourceDetails().tag("관리자").description("관리자 신고 내역 전체 조회")
                .responseSchema(Schema.schema("ReportHTTP.response")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("JSESSION").description("세션")
            ),
            responseFields(
                fieldWithPath("contents").description("List of report details"),
                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                fieldWithPath("contents.[].targetedNickname").type(JsonFieldType.STRING)
                    .description("신고 대상 닉네임"),
                fieldWithPath("contents.[].chattingRoomName").type(JsonFieldType.STRING)
                    .description("신고 대상 채팅방 이름"),
                fieldWithPath("contents.[].targetedId").type(JsonFieldType.NUMBER)
                    .description("신고 대상 아이디"),
                fieldWithPath("contents.[].chattingRoomId").type(JsonFieldType.NUMBER)
                    .description("신고 대상 채팅방 이름"),
                fieldWithPath("contents.[].createdAt").type(JsonFieldType.STRING)
                    .description("신고 생성 날짜")
            )
        ))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(result)));
  }

  @Test
  @DisplayName("동일 채팅방 내의 신고 대상에 대한 신고 내역을 조회할 수 있다.")
  void findReportByTargetIdAndChattingRoomIdTest() throws Exception {
    // given
    List<GroupReportDto> response = List.of(GroupReportDtoFixture.targetReportDto(),
        GroupReportDtoFixture.targetReportDto());
    GroupReportList resultResponse = GroupReportListFixture.groupReportListResponse(response);

    given(reportService.findReportByTargetIdAndChattingRoomId(anyLong(), anyLong())).willReturn(
        response);

    // when, then
    mockMvc.perform(get("/api/v1/admins/reports/group")
            .header("JSESSION", SESSION_VALUE)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON)
            .param("targetedId", String.valueOf(1L))
            .param("chattingRoomId", String.valueOf(1L)))
        .andDo(document("find-reports-by-targetedId-and-chattingRoomId",
            resourceDetails().tag("관리자").description("신고 대상과 채팅방 아이디로 관리자 신고 내역 조회")
                .responseSchema(Schema.schema("TargetReportHTTP.Response")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
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
                fieldWithPath("reports.[].reportId").type(JsonFieldType.NUMBER)
                    .description("신고자 닉네임"),
                fieldWithPath("reports.[].createdAt").type(JsonFieldType.STRING)
                    .description("신고 생성 날짜")
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
    ReportDetailDto response = ReportDetailDtoFixture.reportDetailDto();
    ReportDetailHTTP.Response expectedResponse = ReportDetailHTTPFixture.reportDetailHTTPResponse(
        response);

    given(reportService.findReportById(anyLong())).willReturn(response);

    // when, then
    mockMvc.perform(get("/api/v1/admins/reports/detail/{reportId}", reportId)
            .header("JSESSION", SESSION_VALUE)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON))
        .andDo(document("find-report-by-id",
                resourceDetails().tag("관리자").description("관리자 신고 내역 조회")
                    .responseSchema(Schema.schema("ReportHTTP.Response")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
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
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("신고 생성 날짜")
                )
            )
        )
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
  }

  @DisplayName("사용자의 문의 내역을 조회할 수 있다.")
  @Test
  void searchInquiriesTest() throws Exception {
    // given
    Long lastInquiryId = 10L;
    int pageSize = 10;

    InquirySearchResponse inquirySearchResponse = InquiryFixture.inquirySearchResponse();
    AdminCustomSlice<InquirySummary> adminCustomSlice = AdminFixture.adminCustomPageByInquiry(
        inquirySearchResponse.contents(), inquirySearchResponse.hasNext());
    given(inquiryService.searchInquiries(lastInquiryId, pageSize)).willReturn(
        inquirySearchResponse);

    // when, then
    mockMvc.perform(get(baseUrl + "/inquiries")
            .header("JSESSION", SESSION_VALUE)
            .param("lastInquiryId", String.valueOf(lastInquiryId))
            .sessionAttr("adminId", "admin")
        )
        .andDo(document("view-all-inquiry",
                resourceDetails().tag("관리자").description("관리자 문의 전체 조회")
                    .responseSchema(Schema.schema("AdminCustomPage<InquirySummary>")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("JSESSION").description("세션")
                ),
                queryParameters(
                    parameterWithName("lastInquiryId").description("마지막 문의 아이디")
                ),
                responseFields(
                    fieldWithPath("contents").type(JsonFieldType.ARRAY)
                        .description("문의 리스트"),
                    fieldWithPath("contents[].inquiryId").type(JsonFieldType.NUMBER)
                        .description("문의 아이디"),
                    fieldWithPath("contents[].inquirer").type(JsonFieldType.STRING)
                        .description("문의자 이름"),
                    fieldWithPath("contents[].title").type(JsonFieldType.STRING)
                        .description("문의 제목"),
                    fieldWithPath("contents[].createdAt").type(JsonFieldType.STRING)
                        .description("문의 생성 날짜"),
                    fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                        .description("다음 페이지 여부")
                )
            )
        )
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(adminCustomSlice)));
  }

  @DisplayName("사용자 문의를 상세 조회할 수 있다.")
  @Test
  void viewInquiryTest() throws Exception {
    // given
    Long inquiryId = 1L;
    InquiryDetailDto response = InquiryFixture.inquiryDetailDto();
    InquiryDetailHTTP.Response expectedResponse = InquiryFixture.inquiryDetailHTTPResponse(
        response);

    given(inquiryService.findInquiryBy(anyLong())).willReturn(response);

    // when, then
    mockMvc.perform(get(baseUrl + "/inquiries/detail/{inquiryId}", inquiryId)
            .header("JSESSION", SESSION_VALUE)
            .sessionAttr("adminId", "admin")
            .contentType(APPLICATION_JSON))
        .andDo(document("view-inquiry",
            resourceDetails().tag("관리자").description("관리자 문의 상세 조회")
                .responseSchema(Schema.schema("InquiryDetailHTTP.Response")),
            pathParameters(
                parameterWithName("inquiryId").description("문의 아이디")
            ),
            requestHeaders(
                headerWithName("JSESSION").description("세션")
            ),
            responseFields(
                fieldWithPath("inquirerId").type(JsonFieldType.NUMBER).description("문의자 아이디"),
                fieldWithPath("inquirerNickname").type(JsonFieldType.STRING)
                    .description("문의자 닉네임"),
                fieldWithPath("inquirerEmail").type(JsonFieldType.STRING).description("문의자 이메일"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("문의 제목"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("문의 내용"),
                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("문의 생성 날짜")
            )))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
  }

  @Test
  @DisplayName("회사 인증 대기중인 목록을 조회할 수 있다.")
  void getPendingCertificationsTest() throws Exception {
    // given
    Pageable pageable = pageable();
    PendingCertificationPageDto pendingCertificationPageDto = pendingCertificationPageDto(
        pageable.getPageSize());
    Page<PendingCertification> page = pendingCertificationPageDto.page();
    AdminCustomPage<PendingCertification> pendingCertificationAdminCustomPage = AdminCustomPage.of(
        page.getContent(), page.hasNext());

    given(certificationService.getUncertifiedUserRequests(any())).willReturn(
        pendingCertificationPageDto);

    // when, then
    mockMvc.perform(get(baseUrl + "/certifications/pending")
            .header("JSESSION", SESSION_VALUE)
            .param("offset", String.valueOf(pageable.getOffset()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .sessionAttr("adminId", "admin")
        )
        .andDo(document("admin-pendingCertifications",
                resourceDetails().tag("관리자").description("회사 인증 요청 조회")
                    .responseSchema(Schema.schema("AdminCustomPage<PendingCertification>")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("JSESSION").description("세션")
                ),
                queryParameters(
                    parameterWithName("offset").description("오프셋"),
                    parameterWithName("size").description("사이즈")
                ),
                responseFields(
                    fieldWithPath("contents").description("대기 중인 회사 인증 목록"),
                    fieldWithPath("contents[].certificationId").description("회사 인증 ID"),
                    fieldWithPath("contents[].nickname").description("닉네임"),
                    fieldWithPath("contents[].companyName").description("회사명"),
                    fieldWithPath("contents[].companyEmail").description("회사 이메일"),
                    fieldWithPath("contents[].businessCardUrl").description("명함 URL"),
                    fieldWithPath("contents[].department").description("부서"),
                    fieldWithPath("hasNext").description("다음 페이지 존재 여부"))
            )
        )
        .andExpect(
            content().string(
                objectMapper.writeValueAsString(pendingCertificationAdminCustomPage)));
  }

  @DisplayName("사용자 문의를 확인할 수 있다.")
  @Test
  void checkInquiryTest() throws Exception {
    // given
    Long inquiryId = 1L;

    // when, then
    mockMvc.perform(patch(baseUrl + "/inquiries/{inquiryId}/check", inquiryId)
            .header(JSESSION, SESSION_VALUE)
            .sessionAttr("adminId", "admin")
        )
        .andDo(document(
                "inquiry-check",
                resourceDetails()
                    .tag("관리자")
                    .description("문의 확인"),
                requestHeaders(
                    headerWithName(JSESSION).description("세션")
                )
            )
        )
        .andExpect(status().isOk());
  }

}
