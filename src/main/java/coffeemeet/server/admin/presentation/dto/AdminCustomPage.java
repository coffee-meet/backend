package coffeemeet.server.admin.presentation.dto;

import java.util.List;

public record AdminCustomPage<T>(
        List<T> contents,
        long totalElements,
        long totalPages
) {

    public static <E> AdminCustomPage<E> of(
            List<E> contents,
            long totalElements,
            long totalPages) {
        return new AdminCustomPage<>(contents, totalElements, totalPages);
    }

}
