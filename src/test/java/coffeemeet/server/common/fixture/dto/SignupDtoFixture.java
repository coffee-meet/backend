package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.dto.SignupDto;
import coffeemeet.server.user.dto.SignupDto.Request;
import java.util.List;
import org.instancio.Instancio;

public class SignupDtoFixture {

  public static SignupDto.Request signupDto() {
    return Instancio.of(SignupDto.Request.class)
        .set(field(Request::keywords), keywords())
        .create();
  }

  public static List<Keyword> keywords() {
    return Instancio.ofList(Keyword.class)
        .size(3)
        .create();
  }

}
