package coffeemeet.server.oauth.infrastructure.kakao.dto;

import coffeemeet.server.oauth.infrastructure.OAuthUnlinkDetail;

public record KakaoUnlinkDetail(Long userId) {

  public OAuthUnlinkDetail toOAuthUnlinkDetail() {
    return new OAuthUnlinkDetail(this.userId(), null, null);
  }

}
