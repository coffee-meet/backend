package coffeemeet.server.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebaseConfig {

  private final String filename;

  public FirebaseConfig(@Value("${cloud.firebase.key-path}") String filename) {
    this.filename = filename;
  }

  @Bean
  FirebaseApp firebaseApp() throws IOException {
    ClassPathResource resource = new ClassPathResource(filename);

    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(resource.getInputStream())).build();

    return FirebaseApp.initializeApp(options);
  }

  @Bean
  FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
    return FirebaseMessaging.getInstance(firebaseApp);
  }

}
