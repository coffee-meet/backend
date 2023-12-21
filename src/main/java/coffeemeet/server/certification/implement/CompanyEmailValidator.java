package coffeemeet.server.certification.implement;

import static coffeemeet.server.certification.exception.CertificationErrorCode.EXISTED_COMPANY_EMAIL;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.common.execption.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyEmailValidator {

  private static final String EXISTED_COMPANY_EMAIL_MESSAGE = "이미 사용 중인 회사 이메일(%s) 입니다.";

  private final CertificationQuery certificationQuery;

  public void validateDuplicatedCompanyEmail(CompanyEmail companyEmail) {
    if (certificationQuery.isExistedCompanyEmail(companyEmail)) {
      throw new InvalidInputException(EXISTED_COMPANY_EMAIL,
          String.format(EXISTED_COMPANY_EMAIL_MESSAGE, companyEmail.getValue()));
    }
  }

}
