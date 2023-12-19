package coffeemeet.server.certification.implement;

import java.util.random.RandomGenerator;
import org.springframework.stereotype.Component;

@Component
public class VerificationCodeGenerator {

  private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.getDefault();

  public String generateVerificationCode() {
    return String.format("%06d", RANDOM_GENERATOR.nextInt(1000000));
  }

}
