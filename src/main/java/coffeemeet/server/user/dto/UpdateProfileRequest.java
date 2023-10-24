package coffeemeet.server.user.dto;

import coffeemeet.server.interest.domain.Keyword;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UpdateProfileRequest(@NotBlank String nickname, List<Keyword> interests) {

}
