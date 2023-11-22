package coffeemeet.server.report.domain;

import coffeemeet.server.common.util.Patterns;

public enum ReportReason {
  혐오_발언,
  괴롭힘,
  성적인_괴롭힘,
  스팸_및_광고,
  부적절한_콘텐츠,
  잠수;

  public static ReportReason getReason(String reason) {
    return ReportReason.valueOf(Patterns.BLANCK_PATTERN.matcher(reason).replaceAll("_"));
  }

}
