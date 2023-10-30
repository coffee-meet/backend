package coffeemeet.server.common.advice;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import coffeemeet.server.common.execption.ErrorCode;
import coffeemeet.server.common.execption.GlobalErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@JsonInclude(NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private String code;
  private String message;
  private List<FieldError> errors;

  private ErrorResponse(final ErrorCode code, List<FieldError> errors) {
    this.code = code.code();
    this.message = code.message();
    this.errors = errors;
  }

  private ErrorResponse(final ErrorCode code) {
    this.message = code.message();
    this.code = code.code();
  }

  public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
    return new ErrorResponse(code, FieldError.of(bindingResult));
  }

  public static ErrorResponse of(final ErrorCode code) {
    return new ErrorResponse(code);
  }

  public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
    return new ErrorResponse(code, errors);
  }

  public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
    final String value = e.getValue() == null ? "" : String.valueOf(e.getValue());
    final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
    return new ErrorResponse(GlobalErrorCode.VALIDATION_ERROR, errors);
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  static class FieldError {

    private String field;
    private String value;
    private String reason;

    private FieldError(final String field, final String value, final String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    public static List<FieldError> of(final String field, final String value, final String reason) {
      List<FieldError> fieldErrors = new ArrayList<>();
      fieldErrors.add(new FieldError(field, value, reason));
      return fieldErrors;
    }

    private static List<FieldError> of(final BindingResult bindingResult) {
      final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
      return fieldErrors.stream()
          .map(error -> new FieldError(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()))
          .toList();
    }
  }

}
