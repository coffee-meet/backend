package coffeemeet.server.chatting.current.domain;


import jakarta.persistence.Id;

public record ChattingSession(
    @Id
    String sessionId,
    Long userId
) {

}
