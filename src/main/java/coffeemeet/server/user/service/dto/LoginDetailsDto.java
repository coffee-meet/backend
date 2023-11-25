package coffeemeet.server.user.service.dto;

import coffeemeet.server.auth.domain.AuthTokens;
import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.util.List;

public record LoginDetailsDto(
        Long userId,
        boolean isRegistered,
        String accessToken,
        String refreshToken,
        String nickname,
        String profileImageUrl,
        String companyName,
        Department department,
        List<Keyword> interests
) {

    public static LoginDetailsDto of(User user, List<Keyword> interests, Certification certification,
                                     AuthTokens authTokens) {
        if (certification == null || authTokens == null) {
            return new LoginDetailsDto(
                    user.getId(),
                    user.isRegistered(),
                    null,
                    null,
                    null,
                    user.getOauthInfo().getProfileImageUrl(),
                    null,
                    null,
                    interests
            );
        }
        return new LoginDetailsDto(
                user.getId(),
                user.isRegistered(),
                authTokens.accessToken(),
                authTokens.refreshToken(),
                user.getProfile().getNickname(),
                user.getOauthInfo().getProfileImageUrl(),
                certification.getCompanyName(),
                certification.getDepartment(),
                interests
        );
    }

}
