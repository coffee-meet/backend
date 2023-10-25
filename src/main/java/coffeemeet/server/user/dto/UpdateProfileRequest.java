package coffeemeet.server.user.dto;

import coffeemeet.server.interest.domain.Keyword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateProfileRequest(@NotBlank String nickname,
                                   @NotBlank String name,
                                   @NotNull @Size(min = 1, max = 3) List<Keyword> interests) {

}
