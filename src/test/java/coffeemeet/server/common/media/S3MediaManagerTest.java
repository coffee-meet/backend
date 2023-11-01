package coffeemeet.server.common.media;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

import com.amazonaws.services.s3.AmazonS3;
import java.io.File;
import java.net.URL;
import org.instancio.Instancio;
import org.instancio.internal.generator.net.URLGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class S3MediaManagerTest {

  private S3MediaManager s3MediaManager;

  @Mock
  private AmazonS3 amazonS3;

  private String bucketName = Instancio.create(String.class);

  private String validKey;

  private KeyType keyType;

  @BeforeEach
  void setUp() {
    keyType = Instancio.create(KeyType.class);
    validKey = keyType.getValue() + "-" + Instancio.create(String.class);
    s3MediaManager = new S3MediaManager(amazonS3, bucketName);
  }

  @Test
  @DisplayName("파일을 업로드 할 수 있다")
  void uploadTest() {
    // given
    File file = mock();

    // when
    s3MediaManager.upload(validKey, file);

    // then
    then(amazonS3).should(only()).putObject(bucketName, validKey, file);
  }

  @Test
  @DisplayName("파일을 삭제 할 수 있다")
  void deleteTest() {
    // given
    willDoNothing().given(amazonS3).deleteObject(bucketName, validKey);

    // when
    s3MediaManager.delete(validKey);

    // then
    then(amazonS3).should(only()).deleteObject(bucketName, validKey);

  }

  @Test
  @DisplayName("파일 URL을 얻을 수 있다")
  void getUrlTest() {
    // given
    URL url = new URLGenerator().get();
    given(amazonS3.getUrl(bucketName, validKey)).willReturn(url);

    // when
    String actualUrl = s3MediaManager.getUrl(validKey);

    // then
    assertThat(actualUrl).isEqualTo(url.toExternalForm());
  }

  @Test
  @DisplayName("s3 키를 생성할 수 있다")
  void generateKey() {
    // given
    KeyType keyType = Instancio.create(KeyType.class);

    // when
    String key = s3MediaManager.generateKey(keyType);

    // then
    assertThat(key).contains(keyType.getValue());
  }

  @Test
  @DisplayName("URL에서 s3 키를 추출할 수 있다")
  void extractKey() {
    // given, when
    String extractedKey = s3MediaManager.extractKey(
        new URLGenerator().get().toExternalForm() + validKey, keyType);

    // then
    assertThat(extractedKey).isEqualTo(validKey);
  }
}
