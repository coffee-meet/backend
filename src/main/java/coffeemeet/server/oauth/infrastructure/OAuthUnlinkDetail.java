package coffeemeet.server.oauth.infrastructure;

public record OAuthUnlinkDetail(
    Long id,
    String accessToken,
    String result
) {

}
