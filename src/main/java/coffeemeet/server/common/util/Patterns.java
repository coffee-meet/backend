package coffeemeet.server.common.util;

import java.util.regex.Pattern;

public class Patterns {

  public static final Pattern EMAIL_PATTERN = Pattern.compile(
      "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

  // TODO: 11/21/23 다른 도메인에서도 사용 될 수 있으니, 네이밍을 좀 더 적절하게 하면 좋을거 같아요 
  public static final Pattern REPORT_REASON_PATTERN = Pattern.compile(" ");
}
