package coffeemeet.server.chatting.current.presentation;

import static coffeemeet.server.common.fixture.AuthFixture.refreshToken;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.chatting.current.presentation.dto.ChattingCustomSlice;
import coffeemeet.server.chatting.current.presentation.dto.ChattingRoomStatusHTTP;
import coffeemeet.server.chatting.current.service.ChattingRoomService;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
import coffeemeet.server.chatting.current.service.dto.ChattingRoomStatusDto;
import coffeemeet.server.common.config.ControllerTestConfig;
import coffeemeet.server.common.fixture.ChattingFixture;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(ChattingRoomController.class)
class ChattingRoomControllerTest extends ControllerTestConfig {

  @MockBean
  private ChattingRoomService chattingRoomService;

  @Test
  @DisplayName("채팅방 내역을 조회할 수 있다.")
  void viewChattingRoomMessagesTest() throws Exception {
    // given
    RefreshToken refreshToken = refreshToken();
    Long roomId = 1L;
    Long lastMessageId = 51L;
    int pageSize = 50;
    ChattingListDto responses = ChattingFixture.chattingListDto();
    ChattingCustomSlice.Response chatsHTTPResponse = ChattingFixture.chatsHTTPResponse(responses);

    given(jwtTokenProvider.extractUserId(TOKEN_BODY)).willReturn(USER_ID);
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);

    given(chattingRoomService.searchMessages(USER_ID, roomId, lastMessageId, pageSize)).willReturn(
        responses);

    // when, then
    mockMvc.perform(get("/api/v1/chatting/rooms/{roomId}", roomId).param("lastMessageId",
            String.valueOf(lastMessageId)).header("Authorization", TOKEN)).andDo(
            document("view-chatting-room", resourceDetails().tag("채팅방").description("채팅방 조회")
                    .responseSchema(Schema.schema(" ChatsHTTP.Response")), preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(headerWithName("Authorization").description("토큰")),
                queryParameters(parameterWithName("lastMessageId").description("첫번째 메세지 아이디")),
                responseFields(
                    fieldWithPath("chats[]").type(JsonFieldType.ARRAY).description("메세지 리스트"),
                    fieldWithPath("chats[].userId").type(JsonFieldType.NUMBER).description("사용자 번호"),
                    fieldWithPath("chats[].profileImageUrl").type(JsonFieldType.STRING)
                        .description("프로필 이미지"),
                    fieldWithPath("chats[].messageId").type(JsonFieldType.NUMBER).description("메세지 번호"),
                    fieldWithPath("chats[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("chats[].content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("chats[].createdAt").type(JsonFieldType.STRING).description("생성 기간"),
                    fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"))))
        .andExpect(content().string(objectMapper.writeValueAsString(chatsHTTPResponse)));
  }

  @Test
  @DisplayName("채팅방을 나갈 수 있다.")
  void exitChattingRoomTest() throws Exception {
    // given
    RefreshToken refreshToken = refreshToken();
    Long requestUserId = 1L;
    Long roomId = 1L;
    Long chattingRoomLastMessageId = 100L;

    given(jwtTokenProvider.extractUserId(TOKEN_BODY)).willReturn(requestUserId);
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
    willDoNothing().given(chattingRoomService)
        .exitChattingRoom(requestUserId, roomId, chattingRoomLastMessageId);

    // when, then
    mockMvc.perform(
            delete("/api/v1/chatting/rooms/{roomId}?chattingRoomLastMessageId={chattingRoomLastMessageId}", roomId,
                chattingRoomLastMessageId).header("Authorization", TOKEN)).andDo(
            document("exit-chatting-room", resourceDetails().tag("채팅방").description("채팅방 나가기"),
                preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                requestHeaders(headerWithName("Authorization").description("토큰")),
                queryParameters(parameterWithName("chattingRoomLastMessageId").description("첫번째 메세지 아이디"))))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("채팅방의 상태를 체크할 수 있다.")
  void checkChattingRoomTest() throws Exception {
    // given
    Long roomId = 1L;
    ChattingRoomStatusDto chattingRoomStatusDto = ChattingFixture.chatRoomStatusDto();
    ChattingRoomStatusHTTP.Response response = ChattingFixture.chatRoomStatusHTTPResponse(
        chattingRoomStatusDto);

    given(chattingRoomService.checkChattingRoomStatus(roomId)).willReturn(chattingRoomStatusDto);

    // when, then
    mockMvc.perform(
            get("/api/v1/chatting/rooms/{roomId}/exist", roomId).header("Authorization", TOKEN)).andDo(
            document("check-chatting-room-status",
                resourceDetails().tag("채팅방").description("채팅방 상태 확인"), preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(headerWithName("Authorization").description("토큰"))))
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

}
