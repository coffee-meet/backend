package coffeemeet.server.oauth.infrastructure.naver.dto;

import coffeemeet.server.oauth.infrastructure.OAuthUnlinkDetail;

public record NaverUnlinkDetail(
    String accessToken,
    String result
) {

  public OAuthUnlinkDetail toOAuthUnlinkDetail() {
    return new OAuthUnlinkDetail(null, this.accessToken, this.result);
  }

}
