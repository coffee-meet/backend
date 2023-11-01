package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.controller.dto.SignupHttpDto;
import coffeemeet.server.user.controller.dto.SignupHttpDto.Request;
import java.util.List;
import org.instancio.Instancio;

public class SignupDtoFixture {

  public static SignupHttpDto.Request signupDto() {
    return Instancio.of(SignupHttpDto.Request.class)
        .set(field(Request::keywords), keywords())
        .create();
  }

  public static List<Keyword> keywords() {
    return Instancio.ofList(Keyword.class)
        .size(3)
        .create();
  }

}
