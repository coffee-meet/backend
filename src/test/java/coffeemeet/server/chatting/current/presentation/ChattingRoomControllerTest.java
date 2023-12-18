package coffeemeet.server.chatting.current.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
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

import coffeemeet.server.chatting.current.presentation.dto.ChatRoomStatusHTTP;
import coffeemeet.server.chatting.current.presentation.dto.ChatsHTTP;
import coffeemeet.server.chatting.current.service.ChattingRoomService;
import coffeemeet.server.chatting.current.service.dto.ChatRoomStatusDto;
import coffeemeet.server.chatting.current.service.dto.ChattingListDto;
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

  @DisplayName("채팅방 내역을 조회할 수 있다.")
  @Test
  void viewChattingRoomMessagesTest() throws Exception {
    // given
    Long userId = 1L;
    Long roomId = 1L;
    Long firstMessageId = 51L;
    int pageSize = 50;
    ChattingListDto responses = ChattingFixture.chattingListDto();
    ChatsHTTP.Response chatsHTTPResponse = ChattingFixture.chatsHTTPResponse(responses);

    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);
    given(chattingRoomService.searchMessages(roomId, firstMessageId, pageSize)).willReturn(
        responses);

    // when, then
    mockMvc.perform(get("/api/v1/chatting/rooms/{roomId}", roomId)
            .param("firstMessageId", String.valueOf(firstMessageId))
            .header("Authorization", TOKEN)
        )
        .andDo(document("view-chatting-room",
                resourceDetails().tag("채팅방").description("채팅방 조회")
                    .responseSchema(Schema.schema(" ChatsHTTP.Response")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                ),
                queryParameters(
                    parameterWithName("firstMessageId").description("첫번째 메세지 아이디")
                ),
                responseFields(
                    fieldWithPath("chats[]").type(JsonFieldType.ARRAY).description("메세지 리스트"),
                    fieldWithPath("chats[].userId").type(JsonFieldType.NUMBER).description("사용자 번호"),
                    fieldWithPath("chats[].profileImageUrl").type(JsonFieldType.STRING)
                        .description("프로필 이미지"),
                    fieldWithPath("chats[].messageId").type(JsonFieldType.NUMBER).description("메세지 번호"),
                    fieldWithPath("chats[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("chats[].content").type(JsonFieldType.STRING)
                        .description("내용"),
                    fieldWithPath("chats[].createdAt").type(JsonFieldType.STRING).description("생성 기간"),
                    fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부")
                )
            )
        )
        .andExpect(content().string(objectMapper.writeValueAsString(chatsHTTPResponse)));
  }

  @DisplayName("채팅방을 나갈 수 있다.")
  @Test
  void exitChattingRoomTest() throws Exception {
    // given
    Long userId = 1L;
    Long roomId = 1L;
    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);
    willDoNothing().given(chattingRoomService).deleteChattingRoom(roomId);

    // when, then
    mockMvc.perform(delete("/api/v1/chatting/rooms/{roomId}", roomId)
            .header("Authorization", TOKEN)
        )
        .andDo(document("exit-chatting-room",
                resourceDetails().tag("채팅방").description("채팅방 나가기"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                )
            )
        )
        .andExpect(status().isOk());
  }

  @DisplayName("채팅방의 상태를 체크할 수 있다.")
  @Test
  void checkChattingRoomTest() throws Exception {
    // given
    Long userId = 1L;
    Long roomId = 1L;
    ChatRoomStatusDto chatRoomStatusDto = ChattingFixture.chatRoomStatusDto();
    ChatRoomStatusHTTP.Response response = ChattingFixture.chatRoomStatusHTTPResponse(
        chatRoomStatusDto);

    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);
    given(chattingRoomService.checkChattingRoomStatus(roomId)).willReturn(chatRoomStatusDto);

    // when, then
    mockMvc.perform(get("/api/v1/chatting/rooms/{roomId}/exist", roomId)
            .header("Authorization", TOKEN)
        )
        .andDo(document("check-chatting-room-status",
                resourceDetails().tag("채팅방").description("채팅방 상태 확인"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("토큰")
                )
            )
        )
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

}
