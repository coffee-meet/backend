package coffeemeet.server.certification.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BusinessCardS3KeyGenerator {

  public String generate() {
    return String.format("BusinessCard-%s-%s", LocalDateTime.now(), UUID.randomUUID());
  }

}
