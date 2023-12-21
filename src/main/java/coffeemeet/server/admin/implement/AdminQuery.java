package coffeemeet.server.admin.implement;


import static coffeemeet.server.common.execption.GlobalErrorCode.VALIDATION_ERROR;

import coffeemeet.server.admin.domain.Admin;
import coffeemeet.server.admin.infrastructure.AdminRepository;
import coffeemeet.server.common.execption.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminQuery {

  private static final String ID_VALIDATION_ERROR_MESSAGE = "유효하지 않은 관리자 아이디(%s)입니다.";
  private static final String PASSWORD_VALIDATION_ERROR_MESSAGE = "유효하지 않은 관리자 비밀번호(%s)입니다.";

  private final AdminRepository adminRepository;

  public void checkIdAndPassword(String id, String password) {
    Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(VALIDATION_ERROR,
            String.format(ID_VALIDATION_ERROR_MESSAGE, id)));

    if (admin.isCorrectPassword(password)) {
      return;
    }
    throw new NotFoundException(VALIDATION_ERROR,
        String.format(PASSWORD_VALIDATION_ERROR_MESSAGE, password));
  }

}
