package coffeemeet.server.common.domain;

import lombok.Getter;

@Getter
public enum S3KeyPrefix {

  BUSINESS_CARD("business-card"),
  PROFILE_IMAGE("profile-image"),
  ;

  private final String value;

  S3KeyPrefix(String value) {
    this.value = value;
  }

}
