package coffeemeet.server.admin.presentation.dto;

import java.util.List;

public record AdminCustomSlice<T>(
    List<T> contents,
    boolean hasNext
) {

  public static <E> AdminCustomSlice<E> of(
      List<E> contents,
      boolean hasNext) {
    return new AdminCustomSlice<>(contents, hasNext);
  }

}
