package coffeemeet.server.common.util;

import java.util.regex.Pattern;

public class Patterns {

  public static final Pattern EMAIL_PATTERN = Pattern.compile(
      "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

  public static final Pattern BLANCK_PATTERN = Pattern.compile(" ");
}
