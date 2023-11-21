package coffeemeet.server.user.presentation;

import static coffeemeet.server.common.fixture.entity.UserFixture.notificationTokenHTTPRequest;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.dto.LoginDetailsDtoFixture;
import coffeemeet.server.common.fixture.dto.LoginDetailsHTTPFixture;
import coffeemeet.server.common.fixture.dto.MyProfileDtoFixture;
import coffeemeet.server.common.fixture.dto.MyProfileHTTPFixture;
import coffeemeet.server.common.fixture.dto.RefreshTokenFixture;
import coffeemeet.server.common.fixture.dto.SignupHTTPFixture;
import coffeemeet.server.common.fixture.dto.UpdateProfileHTTPFixture;
import coffeemeet.server.common.fixture.dto.UserProfileDtoFixture;
import coffeemeet.server.common.fixture.dto.UserProfileHTTPFixture;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.presentation.dto.LoginDetailsHTTP;
import coffeemeet.server.user.presentation.dto.MyProfileHTTP;
import coffeemeet.server.user.presentation.dto.NotificationTokenHTTP;
import coffeemeet.server.user.presentation.dto.SignupHTTP;
import coffeemeet.server.user.presentation.dto.UpdateProfileHTTP;
import coffeemeet.server.user.presentation.dto.UserProfileHTTP;
import coffeemeet.server.user.service.UserService;
import coffeemeet.server.user.service.dto.LoginDetailsDto;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.UserProfileDto;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTestConfig {

  @MockBean
  private UserService userService;

  @BeforeEach
  void setUp() {
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
  }

  @Test
  @DisplayName("회원가입을 할 수 있다.")
  void signupTest() throws Exception {
    // given
    SignupHTTP.Request request = SignupHTTPFixture.signupHTTPRequest();

    willDoNothing().given(userService).signup(any(), any(), anyList());

    // when, then
    mockMvc.perform(post("/api/v1/users/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andDo(document("user-signup",
            resourceDetails().tag("사용자").description("회원가입")
                .requestSchema(Schema.schema("SignupHTTP.Request"))
                .responseSchema(Schema.schema("AuthTokens")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("keywords").type(JsonFieldType.ARRAY).description("관심사"),
                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 아이디")
            )
        ))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("로그인을 할 수 있다.")
  void loginTest() throws Exception {
    // given
    LoginDetailsDto.Response response = LoginDetailsDtoFixture.loginDetailsDto();
    LoginDetailsHTTP.Response expectedResponse = LoginDetailsHTTPFixture.loginDetailsHTTPResponse(
        response);

    given(userService.login(any(), any())).willReturn(response);

    // when, then
    mockMvc.perform(get("/api/v1/users/login/{oAuthProvider}", OAuthProvider.KAKAO)
            .contentType(MediaType.APPLICATION_JSON)
            .param("authCode", "authCode"))
        .andDo(document("user-login",
            resourceDetails().tag("사용자").description("로그인")
                .responseSchema(Schema.schema("LoginDetailsHTTP")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("oAuthProvider").description("OAuth 제공자")
            ),
            queryParameters(
                parameterWithName("authCode").description("인증 코드")
            ),
            responseFields(
                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                fieldWithPath("isRegistered").type(JsonFieldType.BOOLEAN).description("회원 가입 여부"),
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                    .description("프로필 사진 url"),
                fieldWithPath("companyName").type(JsonFieldType.STRING).description("회사 이름"),
                fieldWithPath("department").type(JsonFieldType.STRING).description("부서"),
                fieldWithPath("interests").type(JsonFieldType.ARRAY).description("관심사")
            )
        ))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
  }

  @Test
  @DisplayName("사용자 프로필을 조회할 수 있다.")
  void findUserProfileTest() throws Exception {
    // given
    long userId = 1L;
    UserProfileDto.Response response = UserProfileDtoFixture.userProfileDtoResponse();
    UserProfileHTTP.Response expectedResponse = UserProfileHTTPFixture.userProfileHTTPResponse(
        response);

    given(userService.findUserProfile(userId)).willReturn(response);

    // when, then
    mockMvc.perform(get("/api/v1/users/{id}", userId)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("user-profile",
                resourceDetails().tag("사용자").description("사용자 프로필 조회")
                    .responseSchema(Schema.schema("UserProfileHTTP.Response")),
                pathParameters(
                    parameterWithName("id").description("유저 아이디")
                ),
                responseFields(
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                        .description("프로필 사진 url"),
                    fieldWithPath("department").type(JsonFieldType.STRING).description("부서"),
                    fieldWithPath("interests").type(JsonFieldType.ARRAY).description("관심사")
                )
            )
        )
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
  }

  @Test
  @DisplayName("마이페이지를 조회할 수 있다.")
  void findMyProfileTest() throws Exception {
    // given
    Long userId = 1L;
    MyProfileDto.Response response = MyProfileDtoFixture.myProfileDtoResponse();
    MyProfileHTTP.Response expectedResponse = MyProfileHTTPFixture.myProfileHTTPResponse(response);

    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);
    given(userService.findMyProfile(anyLong())).willReturn(response);

    // when, then
    mockMvc.perform(get("/api/v1/users/me")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("my-profile",
                resourceDetails().tag("사용자").description("마이페이지 조회")
                    .responseSchema(Schema.schema("MyProfileDto.Response")),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                ),
                responseFields(
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                        .description("프로필 사진 url"),
                    fieldWithPath("reportedCount").type(JsonFieldType.NUMBER).description("신고 횟수"),
                    fieldWithPath("sanctionPeriod").type(JsonFieldType.STRING).description("제재 기간"),
                    fieldWithPath("department").type(JsonFieldType.STRING).description("부서"),
                    fieldWithPath("interests").type(JsonFieldType.ARRAY).description("관심사")
                )
            )
        )
        .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
  }

  @Test
  @DisplayName("본인 프로필 사진을 수정할 수 있다.")
  void updateProfileImageTest() throws Exception {
    // given
    Long userId = 1L;

    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);

    MockMultipartFile file = new MockMultipartFile("image",
        "test.png",
        "image/png",
        "testImage".getBytes());

    // when, then
    mockMvc.perform((multipart("/api/v1/users/me/profile-image")
            .file("profileImage", file.getBytes())
            .header("Authorization", TOKEN)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        ))
        .andDo(document("update-profile-image",
                resourceDetails().tag("사용자").description("본인 프로필 사진 수정")
                    .requestSchema(Schema.schema("MultipartFile")),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                ),
                requestParts(
                    partWithName("profileImage").description("새 프로필 사진")
                )
            )
        )
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("본인 프로필 정보를 수정할 수 있다.")
  void updateProfileInfoTest() throws Exception {
    // given
    Long userId = 1L;
    UpdateProfileHTTP.Request request = UpdateProfileHTTPFixture.updateProfileHTTPRequest();

    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);
    willDoNothing().given(
        userService).updateProfileInfo(any(), any(), any());

    // when, then
    mockMvc.perform(patch("/api/v1/users/me")
            .header("Authorization", TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andDo(document("update-my-profile",
                resourceDetails().tag("사용자").description("본인 프로필 정보 수정")
                    .requestSchema(Schema.schema("UpdateProfileDto.Request")),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                ),
                requestFields(
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("interests").type(JsonFieldType.ARRAY).description("관심사")
                )
            )
        )
        .andExpect(status().isOk()
        );
  }

  @Test
  @DisplayName("닉네임 중복을 확인할 수 있다.")
  void checkNicknameDuplicateTest() throws Exception {
    // given
    User user = user();
    String nickname = user.getProfile().getNickname();

    // when, then
    mockMvc.perform(get("/api/v1/users/duplicate")
            .param("nickname", nickname)
        )
        .andDo(document("check-nickname",
            resourceDetails().tag("사용자").description("닉네임 중복 확인"),
            queryParameters(
                parameterWithName("nickname").description("닉네임")
            )
        ))
        .andExpect(status().isOk()
        );
  }

  @Test
  @DisplayName("FCM 토큰을 등록하거나 업데이트할 수 있다.")
  void registerOrUpdateNotificationTokenTest() throws Exception {
    // given
    NotificationTokenHTTP.Request request = notificationTokenHTTPRequest();

    // when, then
    mockMvc.perform(put("/api/v1/users/notification/token")
            .header(AUTHORIZATION, TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andDo(document("register-or-update-token",
            resourceDetails().tag("사용자").description("토큰 등록 및 업데이트"),
            requestHeaders(
                headerWithName(AUTHORIZATION).description("토큰")
            ),
            requestFields(
                fieldWithPath("token").type(JsonFieldType.STRING).description("토큰")
            )
        ))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("푸시 알림을 거부할 수 있다.")
  void unsubscribeNotificationTest() throws Exception {
    // given, when, then
    mockMvc.perform(put("/api/v1/users/notification/unsubscription")
            .header(AUTHORIZATION, TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
        ).andDo(document("unsubscribe-notification",
            resourceDetails().tag("사용자").description("알림 거부"),
            requestHeaders(
                headerWithName(AUTHORIZATION).description("토큰")
            )
        ))
        .andExpect(status().isOk());
  }

}
