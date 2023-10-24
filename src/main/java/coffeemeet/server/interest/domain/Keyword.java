package coffeemeet.server.interest.domain;

public enum Keyword {
  COOK,
  GAME,
  EXERCISE,
  ;

  public static boolean isValidKeyword(String keyword) {
    for (Keyword key : Keyword.values()) {
      if (key.name().equals(keyword)) {
        return true;
      }
    }
    return false;
  }

}
