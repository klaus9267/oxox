package kimandhong.oxox.application.handler;

import jakarta.servlet.http.HttpServletRequest;
import kimandhong.oxox.application.handler.error.ErrorCode;
import kimandhong.oxox.application.handler.error.ErrorResponse;
import kimandhong.oxox.application.handler.error.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    log.error("[Runtime] : ", exception);
    final ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler({NotFoundException.class, BadRequestException.class, ConflictException.class, ForbiddenException.class, S3Exception.class})
  public ResponseEntity<ErrorResponse> handleCustomException(final BaseException exception, final HttpServletRequest request) {
    log.error("[" + exception.getClass().getSimpleName() + "] : " + exception.getMessage());
    final ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode(), exception.getMessage());

    return ResponseEntity.status(exception.getErrorCode().getHttpStatus()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException exception, final HttpServletRequest request) {
    List<String> errorMessages = exception.getBindingResult()
        .getAllErrors()
        .stream()
        .map(FieldError.class::cast)
        .map(FieldError::getDefaultMessage)
        .toList();

    log.error("[MethodArgumentNotValidException] : " + errorMessages);
    final ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST, errorMessages);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  // bulk exception
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(final DataIntegrityViolationException exception, final HttpServletRequest request) {
    log.error("[DataIntegrityViolationException] : ", exception);
    final ErrorResponse errorResponse = new ErrorResponse(ErrorCode.CONFLICT, "Data integrity violation");

    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }
}
