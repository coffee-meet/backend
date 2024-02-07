package coffeemeet.server.admin.implement;

import static coffeemeet.server.common.execption.GlobalErrorCode.VALIDATION_ERROR;

import coffeemeet.server.admin.domain.Admin;
import coffeemeet.server.common.execption.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountValidator {

  private static final String PASSWORD_VALIDATION_ERROR_MESSAGE = "유효하지 않은 관리자 비밀번호(%s)입니다.";

  private final AdminQuery adminQuery;

  public void validate(String id, String password) {
    Admin admin = adminQuery.getById(id);
    if (!admin.isCorrectPassword(password)) {
      throw new NotFoundException(VALIDATION_ERROR,
          String.format(PASSWORD_VALIDATION_ERROR_MESSAGE, password));
    }
  }

}
