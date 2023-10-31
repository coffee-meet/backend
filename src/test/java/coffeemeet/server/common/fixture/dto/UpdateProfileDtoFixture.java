package coffeemeet.server.common.fixture.dto;

import static org.instancio.Select.field;

import coffeemeet.server.interest.domain.Keyword;
import coffeemeet.server.user.dto.UpdateProfileDto;
import java.util.List;
import org.instancio.Instancio;

public class UpdateProfileDtoFixture {

  public static UpdateProfileDto.Request updateProfileDtoRequest() {
    return Instancio.of(UpdateProfileDto.Request.class)
        .set(field(UpdateProfileDto.Request::interests), keywords())
        .create();
  }

  public static List<Keyword> keywords() {
    return Instancio.ofList(Keyword.class)
        .size(3)
        .create();
  }

}
