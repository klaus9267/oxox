package kimandhong.oxox.application.handler.error.exception;

import kimandhong.oxox.application.handler.error.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends BaseException {
  public BadRequestException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
