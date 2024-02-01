package coffeemeet.server.admin.implement;


import static coffeemeet.server.admin.exception.AdminErrorCode.INVALID_LOGIN_REQUEST;

import coffeemeet.server.admin.domain.Admin;
import coffeemeet.server.admin.domain.AdminRepository;
import coffeemeet.server.common.execption.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminQuery {

  private static final String ID_VALIDATION_ERROR_MESSAGE = "유효하지 않은 관리자 아이디(%s)입니다.";

  private final AdminRepository adminRepository;

  public Admin getById(String id) {
    return adminRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(INVALID_LOGIN_REQUEST,
            String.format(ID_VALIDATION_ERROR_MESSAGE, id)));
  }

}
