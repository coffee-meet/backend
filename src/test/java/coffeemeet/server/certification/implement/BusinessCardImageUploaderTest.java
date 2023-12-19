package coffeemeet.server.certification.implement;

import static coffeemeet.server.common.domain.S3KeyPrefix.BUSINESS_CARD;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import coffeemeet.server.common.implement.ImageUploader;
import java.io.File;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BusinessCardImageUploaderTest {

  @InjectMocks
  private BusinessCardImageUploader businessCardImageUploader;
  @Mock
  private ImageUploader imageUploader;

  @Test
  void uploadBusinessCardImage() {
    // given
    File businessCardImage = Instancio.create(File.class);

    // when
    businessCardImageUploader.uploadBusinessCardImage(businessCardImage);

    // then
    then(imageUploader).should(only()).uploadImage(businessCardImage, BUSINESS_CARD);
  }

}
