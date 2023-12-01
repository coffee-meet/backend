package coffeemeet.server.common.domain;

import lombok.Getter;

@Getter
public enum KeyType {

  BUSINESS_CARD("business-card"),
  PROFILE_IMAGE("profile-image"),
  ;

  private final String value;

  KeyType(String value) {
    this.value = value;
  }

}
