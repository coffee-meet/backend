package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.controller.dto.UpdateProfileHttpDto;
import java.util.List;
import org.instancio.Instancio;

public class UpdateProfileDtoFixture {

  public static UpdateProfileHttpDto.Request updateProfileDtoRequest() {
    return Instancio.of(UpdateProfileHttpDto.Request.class)
        .set(field(UpdateProfileHttpDto.Request::interests), keywords())
        .create();
  }

  public static List<Keyword> keywords() {
    return Instancio.ofList(Keyword.class)
        .size(3)
        .create();
  }

}
