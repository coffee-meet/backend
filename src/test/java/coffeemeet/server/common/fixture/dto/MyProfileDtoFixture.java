package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.service.dto.MyProfileDto;
import coffeemeet.server.user.service.dto.MyProfileDto.Response;
import java.time.LocalDateTime;
import java.util.List;
import org.instancio.Instancio;

public class MyProfileDtoFixture {

  public static MyProfileDto.Response myProfileDtoResponse() {
    return Instancio.of(MyProfileDto.Response.class)
        .set(field(Response::interests), keywords())
        .set(field("email"), "test123@gmail.com")
        .set(field("sanctionPeriod"), LocalDateTime.of(1999, 10, 13, 1, 1, 1, 1))
        .create();
  }

  public static List<Keyword> keywords() {
    return Instancio.ofList(Keyword.class)
        .size(3)
        .create();
  }

}
