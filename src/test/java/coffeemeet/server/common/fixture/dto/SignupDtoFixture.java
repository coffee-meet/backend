package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.presentation.dto.SignupHTTP;
import coffeemeet.server.user.presentation.dto.SignupHTTP.Request;
import java.util.List;
import org.instancio.Instancio;

public class SignupDtoFixture {

  public static SignupHTTP.Request signupDto() {
    return Instancio.of(SignupHTTP.Request.class)
        .set(field(Request::keywords), keywords())
        .create();
  }

  public static List<Keyword> keywords() {
    return Instancio.ofList(Keyword.class)
        .size(3)
        .create();
  }

}
