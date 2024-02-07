package coffeemeet.server.admin.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.admin.domain.Admin;
import coffeemeet.server.admin.domain.AdminRepository;
import coffeemeet.server.common.execption.NotFoundException;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminQueryTest {

  @InjectMocks
  private AdminQuery adminQuery;

  @Mock
  private AdminRepository adminRepository;

  @Nested
  @DisplayName("id로 Admin을 조회할 수 있다.")
  class nested {

    @Test
    @DisplayName("성공")
    void success() {
      // given
      Admin admin = Instancio.create(Admin.class);
      String id = admin.getId();

      given(adminRepository.findById(id)).willReturn(Optional.of(admin));

      // when
      Admin ret = adminQuery.getById(id);

      // then
      assertThat(ret.getId()).isEqualTo(admin.getId());
    }

    @Test
    @DisplayName("존재하지 않는 ID")
    void NotFoundException() {
      // given
      String id = "1";
      given(adminRepository.findById(anyString())).willReturn(Optional.empty());

      // when, then
      assertThatThrownBy(() -> adminQuery.getById(id))
          .isInstanceOf(NotFoundException.class);
    }

  }

}
