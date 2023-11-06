package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.user.domain.Keyword;
import coffeemeet.server.user.presentation.dto.UpdateProfileHTTP;
import java.util.List;
import org.instancio.Instancio;

public class UpdateProfileDtoFixture {

  public static UpdateProfileHTTP.Request updateProfileDtoRequest() {
    return Instancio.of(UpdateProfileHTTP.Request.class)
        .set(field(UpdateProfileHTTP.Request::interests), keywords())
        .create();
  }

  public static List<Keyword> keywords() {
    return Instancio.ofList(Keyword.class)
        .size(3)
        .create();
  }

}
