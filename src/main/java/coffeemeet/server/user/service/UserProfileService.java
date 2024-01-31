package coffeemeet.server.user.service;

import static coffeemeet.server.common.domain.S3KeyPrefix.PROFILE_IMAGE;
import static coffeemeet.server.oauth.utils.constant.OAuthConstant.DEFAULT_IMAGE_URL;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.implement.CertificationQuery;
import coffeemeet.server.common.infrastructure.S3ObjectStorage;
import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.InterestCommand;
import coffeemeet.server.user.implement.InterestQuery;
import coffeemeet.server.user.implement.UserCommand;
import coffeemeet.server.user.implement.UserQuery;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.UserProfileDto;
import jakarta.transaction.Transactional;
import java.io.File;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserQuery userQuery;
  private final InterestQuery interestQuery;
  private final CertificationQuery certificationQuery;
  private final S3ObjectStorage objectStorage;
  private final UserCommand userCommand;
  private final InterestCommand interestCommand;

  public UserProfileDto findUserProfile(Long userId) {
    User user = userQuery.getUserById(userId);
    List<Keyword> keywords = interestQuery.getKeywordsByUserId(userId);
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    return UserProfileDto.of(user, keywords, certification);
  }

  public MyProfileDto findMyProfile(Long userId) {
    User user = userQuery.getUserById(userId);
    List<Keyword> keywords = interestQuery.getKeywordsByUserId(userId);
    Certification certification = certificationQuery.getCertificationByUserId(userId);
    return MyProfileDto.of(user, keywords, certification);
  }

  public void updateProfileImage(Long userId, File file) {
    User user = userQuery.getUserById(userId);
    deleteCurrentProfileImage(user.getOauthInfo().getProfileImageUrl());

    String key = objectStorage.generateKey(PROFILE_IMAGE);
    objectStorage.upload(key, file);
    user.updateProfileImageUrl(objectStorage.getUrl(key));
    userCommand.updateUser(user);
  }

  @Transactional
  public void updateProfileInfo(Long userId, String nickname,
      List<Keyword> keywords) {
    User user = userQuery.getUserById(userId);
    if (nickname != null) {
      userCommand.updateUserInfo(user, nickname);
    }
    if (keywords != null && !keywords.isEmpty()) {
      interestCommand.updateInterests(user, keywords);
    }
  }

  private void deleteCurrentProfileImage(String profileImageUrl) {
    if (!profileImageUrl.equals(DEFAULT_IMAGE_URL)) {
      String currentKey = objectStorage.extractKey(profileImageUrl,
          PROFILE_IMAGE);
      if (currentKey.isBlank()) {
        return;
      }
      objectStorage.delete(currentKey);
    }
  }

}
