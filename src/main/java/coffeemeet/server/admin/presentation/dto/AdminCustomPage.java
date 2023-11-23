package coffeemeet.server.admin.presentation.dto;

import java.util.List;

public record AdminCustomPage<T>(
        List<T> contents,
        boolean hasNext
) {

    public static <E> AdminCustomPage<E> of(
            List<E> contents,
            boolean hasNext) {
        return new AdminCustomPage<>(contents, hasNext);
    }

}
