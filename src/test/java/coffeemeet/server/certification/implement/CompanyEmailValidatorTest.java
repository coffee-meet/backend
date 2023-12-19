package coffeemeet.server.certification.implement;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.common.execption.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyEmailValidatorTest {

  @InjectMocks
  private CompanyEmailValidator companyEmailValidator;
  @Mock
  private CertificationQuery certificationQuery;

  @Test
  @DisplayName("회사 이메일이 중복되면 예외가 발생한다.")
  void validateDuplicatedCompanyEmailTest_InvalidInputException() {
    // given
    CompanyEmail companyEmail = new CompanyEmail("test@example.com");
    when(certificationQuery.isExistedCompanyEmail(companyEmail)).thenReturn(true);

    // when, then
    assertThrows(InvalidInputException.class, () ->
        companyEmailValidator.validateDuplicatedCompanyEmail(companyEmail));
  }

  @Test
  @DisplayName("회사 이메일이 중복되지 않으면 예외가 발생하지 않는다.")
  void validateDuplicatedCompanyEmailTest() {
    // given
    CompanyEmail companyEmail = new CompanyEmail("unique@example.com");
    when(certificationQuery.isExistedCompanyEmail(companyEmail)).thenReturn(false);

    // when, then
    companyEmailValidator.validateDuplicatedCompanyEmail(companyEmail);
  }

}
