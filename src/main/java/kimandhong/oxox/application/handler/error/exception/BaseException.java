package kimandhong.oxox.application.handler.error.exception;

import kimandhong.oxox.application.handler.error.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
  private final ErrorCode errorCode;

  protected BaseException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
