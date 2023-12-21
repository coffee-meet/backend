package coffeemeet.server.chatting.history.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.chatting.history.presentation.dto.ChattingHistoryCustomSlice.Response;
import coffeemeet.server.chatting.history.presentation.dto.ChattingRoomHistoriesHTTP;
import coffeemeet.server.chatting.history.service.ChattingRoomHistoryService;
import coffeemeet.server.chatting.history.service.dto.ChattingHistoryListDto;
import coffeemeet.server.chatting.history.service.dto.ChattingRoomHistoryDto;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.AuthFixture;
import coffeemeet.server.common.fixture.ChattingFixture;
import com.epages.restdocs.apispec.Schema;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(ChattingRoomHistoryController.class)
class ChattingRoomHistoryControllerTest extends ControllerTestConfig {

  @MockBean
  private ChattingRoomHistoryService chattingRoomHistoryService;

  @DisplayName("사용자의 채팅방 내역을 전체 조회할 수 있다.")
  @Test
  void viewChattingRoomHistoriesTest() throws Exception {
    // given
    Long userId = 1L;
    int pageSize = 50;

    RefreshToken refreshToken = AuthFixture.refreshToken();
    List<ChattingRoomHistoryDto> responses = ChattingFixture.chattingRoomHistoryDtoResponses(
        pageSize);
    ChattingRoomHistoriesHTTP.Response response = ChattingFixture.chattingRoomHistoriesHTTP(
        responses);

    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
    given(chattingRoomHistoryService.searchChattingRoomHistories(anyLong())).willReturn(responses);

    // when, then
    mockMvc.perform(get("/api/v1/chatting/room/histories")
            .header("Authorization", TOKEN)
        )
        .andDo(document("view-chatting-room-history",
                resourceDetails().tag("채팅방 내역").description("채팅방 내역 조회")
                    .responseSchema(Schema.schema(" ChattingRoomHistoriesHTTP.Response")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                ),
                responseFields(
                    fieldWithPath("chatRoomHistories[]").type(JsonFieldType.ARRAY)
                        .description("메세지 리스트"),
                    fieldWithPath("chatRoomHistories[].roomId").type(JsonFieldType.NUMBER)
                        .description("채팅방 내역 번호"),
                    fieldWithPath("chatRoomHistories[].roomName").type(JsonFieldType.STRING)
                        .description("채팅방 내역 이름"),
                    fieldWithPath("chatRoomHistories[].users").type(JsonFieldType.ARRAY)
                        .description("채팅방 사용자 닉네임"),
                    fieldWithPath("chatRoomHistories[].createdAt").type(JsonFieldType.STRING)
                        .description("생성 시간")
                )
            )
        )
        .andExpect(content().string(objectMapper.writeValueAsString(response)));

  }

  @DisplayName("채팅방의 메세지들을 조회할 수 있다.")
  @Test
  void viewChattingRoomMessageHistoriesTest() throws Exception {
    // given
    Long userId = 1L;
    Long roomHistoryId = 1L;
    Long firstMessageId = 51L;
    int pageSize = 50;

    ChattingHistoryListDto chattingHistoryListDto = ChattingFixture.chattingMessageHistoryListDto();
    Response response = ChattingFixture.chattingMessageHistoriesHTTPResponse(
        chattingHistoryListDto);

    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);
    given(chattingRoomHistoryService.searchChattingMessageHistories(roomHistoryId, firstMessageId,
        pageSize)).willReturn(
        chattingHistoryListDto);

    // when, then
    mockMvc.perform(get("/api/v1/chatting/room/histories/{roomHistoryId}", roomHistoryId)
            .param("firstMessageId", String.valueOf(firstMessageId))
            .header("Authorization", TOKEN)
        )
        .andDo(document("view-chatting-message-history",
                resourceDetails().tag("채팅방 내역").description("채팅방 메세지 내역 조회")
                    .responseSchema(Schema.schema(" ChattingMessageHistoriesHTTP.Response")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                ),
                queryParameters(
                    parameterWithName("firstMessageId").description("첫번째 메세지 아이디")
                ),
                responseFields(
                    fieldWithPath("chatHistories[]").type(JsonFieldType.ARRAY).description("메세지 리스트"),
                    fieldWithPath("chatHistories[].userId").type(JsonFieldType.NUMBER)
                        .description("사용자 번호"),
                    fieldWithPath("chatHistories[].profileImageUrl").type(JsonFieldType.STRING)
                        .description("프로필 이미지"),
                    fieldWithPath("chatHistories[].messageId").type(JsonFieldType.NUMBER)
                        .description("메세지 번호"),
                    fieldWithPath("chatHistories[].nickname").type(JsonFieldType.STRING)
                        .description("닉네임"),
                    fieldWithPath("chatHistories[].content").type(JsonFieldType.STRING)
                        .description("내용"),
                    fieldWithPath("chatHistories[].createdAt").type(JsonFieldType.STRING)
                        .description("생성 시간"),
                    fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                )
            )
        )
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

}
