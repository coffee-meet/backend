package coffeemeet.server.admin.implement;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import coffeemeet.server.admin.infrastructure.AdminRepository;
import coffeemeet.server.common.execption.NotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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

  @Test
  @DisplayName("아이디와 비밀번호 확인 시 관리자가 존재하지 않는다면 예외를 던진다.")
  void checkIdAndPasswordFailTest() {
    // given
    String id = "1";
    String password = "1";

    given(adminRepository.findById(anyString())).willReturn(Optional.empty());

    // when, then
    assertThatThrownBy(() -> adminQuery.checkIdAndPassword(id, password))
        .isInstanceOf(NotFoundException.class);
  }

}
