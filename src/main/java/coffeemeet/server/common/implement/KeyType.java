package coffeemeet.server.common.implement;

import lombok.Getter;

@Getter
public enum KeyType {

  BUSINESS_CARD("BusinessCard"),
  PROFILE_IMAGE("ProfileImage"),
  ;

  private final String value;

  KeyType(String value) {
    this.value = value;
  }

}
