package coffeemeet.server.user.controller;

import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.dto.MyProfileDtoFixture;
import coffeemeet.server.common.fixture.dto.RefreshTokenFixture;
import coffeemeet.server.common.fixture.dto.SignupDtoFixture;
import coffeemeet.server.common.fixture.dto.UpdateProfileDtoFixture;
import coffeemeet.server.common.fixture.dto.UserProfileDtoFixture;
import coffeemeet.server.user.controller.dto.SignupHttpDto;
import coffeemeet.server.user.controller.dto.UpdateProfileHttpDto.Request;
import coffeemeet.server.user.domain.OAuthProvider;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.service.UserService;
import coffeemeet.server.user.service.dto.MyProfileDto.Response;
import coffeemeet.server.user.service.dto.UserProfileDto;
import com.epages.restdocs.apispec.Schema;
import java.time.format.DateTimeFormatter;
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

  @Test
  @DisplayName("회원가입을 할 수 있다.")
  void signupTest() throws Exception {
    // given
    SignupHttpDto.Request request = SignupDtoFixture.signupDto();
    AuthTokens authTokens = new AuthTokens("accessToken", "refreshToken");

    given(userService.signup(any(), any(), any(), any())).willReturn(authTokens);

    // when, then
    mockMvc.perform(post("/api/v1/users/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andDo(document("user-signup",
            resourceDetails().tag("사용자").description("회원가입")
                .requestSchema(Schema.schema("SignupDto.Request"))
                .responseSchema(Schema.schema("AuthTokens")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("keywords").type(JsonFieldType.ARRAY).description("관심사"),
                fieldWithPath("authCode").type(JsonFieldType.STRING).description("인증 코드"),
                fieldWithPath("oAuthProvider").type(JsonFieldType.STRING).description("OAuth 제공자")
            ),
            responseFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
            )
        ))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("accessToken"))
        .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
  }

  @Test
  @DisplayName("로그인을 할 수 있다.")
  void loginTest() throws Exception {
    // given
    AuthTokens authTokens = new AuthTokens("accessToken", "refreshToken");

    given(userService.login(any(), any())).willReturn(authTokens);

    // when, then
    mockMvc.perform(get("/api/v1/users/login/{oAuthProvider}", OAuthProvider.KAKAO)
            .contentType(MediaType.APPLICATION_JSON)
            .param("authCode", "authCode"))
        .andDo(document("user-login",
            resourceDetails().tag("사용자").description("로그인")
                .responseSchema(Schema.schema("AuthTokens")),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("oAuthProvider").description("OAuth 제공자")
            ),
            queryParameters(
                parameterWithName("authCode").description("인증 코드")
            ),
            responseFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
            )
        ))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("accessToken"))
        .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
  }

  @Test
  @DisplayName("사용자 프로필을 조회할 수 있다.")
  void findUserProfileTest() throws Exception {
    // given
    long userId = 1L;
    UserProfileDto.Response response = UserProfileDtoFixture.userProfileDtoResponse();

    given(userService.findUserProfile(userId)).willReturn(response);

    // when, then
    mockMvc.perform(get("/api/v1/users/{id}", userId)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(document("user-profile",
                resourceDetails().tag("사용자").description("사용자 프로필 조회")
                    .responseSchema(Schema.schema("UserProfileDto.Response")),
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
        .andExpect(jsonPath("$.nickname").value(response.nickname()))
        .andExpect(jsonPath("$.profileImageUrl").value(response.profileImageUrl()))
        .andExpect(jsonPath("$.department").value(String.valueOf(response.department())))
        .andExpect(jsonPath("$.interests[0]").value(response.interests().get(0).name()));
  }

  @Test
  @DisplayName("마이페이지를 조회할 수 있다.")
  void findMyProfileTest() throws Exception {
    // given
    Long userId = 1L;
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();
    Response response = MyProfileDtoFixture.myProfileDtoResponse();

    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
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
                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                        .description("프로필 사진 url"),
                    fieldWithPath("birthYear").type(JsonFieldType.STRING).description("생일년도"),
                    fieldWithPath("birthDay").type(JsonFieldType.STRING).description("생일월일"),
                    fieldWithPath("reportedCount").type(JsonFieldType.NUMBER).description("신고 횟수"),
                    fieldWithPath("sanctionPeriod").type(JsonFieldType.STRING).description("제재 기간"),
                    fieldWithPath("department").type(JsonFieldType.STRING).description("부서"),
                    fieldWithPath("interests").type(JsonFieldType.ARRAY).description("관심사")
                )
            )
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(response.name()))
        .andExpect(jsonPath("$.nickname").value(response.nickname()))
        .andExpect(jsonPath("$.email").value(response.email()))
        .andExpect(jsonPath("$.profileImageUrl").value(response.profileImageUrl()))
        .andExpect(jsonPath("$.birthYear").value(response.birthYear()))
        .andExpect(jsonPath("$.birthDay").value(response.birthDay()))
        .andExpect(jsonPath("$.reportedCount").value(response.reportedCount()))
        .andExpect(
            jsonPath("$.sanctionPeriod").value(response.sanctionPeriod()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"))))
        .andExpect(jsonPath("$.department").value(String.valueOf(response.department())))
        .andExpect(jsonPath("$.interests[0]").value(response.interests().get(0).name()));
  }

  @Test
  @DisplayName("본인 프로필 사진을 수정할 수 있다.")
  void updateProfileImageTest() throws Exception {
    // given
    Long userId = 1L;
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();

    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);

    MockMultipartFile file = new MockMultipartFile("image",
        "test.png",
        "image/png",
        "testImage".getBytes());

    // when, given
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
    Request request = UpdateProfileDtoFixture.updateProfileDtoRequest();
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();

    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);
    willDoNothing().given(
        userService).updateProfileInfo(any(), any(), any());

    // when, given
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
  void checkNicknameDuplicationTest() throws Exception {
    // given
    User user = user();
    String nickname = user.getProfile().getNickname();
    RefreshToken refreshToken = RefreshTokenFixture.refreshToken();

    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);

    // when, given
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

}
