package coffeemeet.server.user.service.dto;

import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import java.util.List;

public record MyProfileDto(
        String nickname,
        String profileImageUrl,
        String companyName,
        Department department,
        List<Keyword> interests
) {

    public static MyProfileDto of(
            User user,
            List<Keyword> interests,
            String companyName,
            Department department
    ) {
        return new MyProfileDto(
                user.getProfile().getNickname(),
                user.getOauthInfo().getProfileImageUrl(),
                companyName,
                department,
                interests
        );
    }

}
