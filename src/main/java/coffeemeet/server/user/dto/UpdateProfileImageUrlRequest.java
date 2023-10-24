package coffeemeet.server.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileImageUrlRequest(@NotBlank String profileImageUrl) {

}
