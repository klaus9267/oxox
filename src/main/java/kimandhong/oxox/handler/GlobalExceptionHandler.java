package kimandhong.oxox.handler;

import jakarta.servlet.http.HttpServletRequest;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleException(final RuntimeException exception, final HttpServletRequest request) {
    log.error("[Runtime] : " + exception.getMessage());
    final ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException exception, final HttpServletRequest request) {
    List<String> errorMessages = exception.getBindingResult()
        .getAllErrors()
        .stream()
        .map(error -> (FieldError) error)
        .map(FieldError::getDefaultMessage)
        .toList();

    log.error("[MethodArgumentNotValidException] : " + errorMessages);
    final ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST, errorMessages);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
