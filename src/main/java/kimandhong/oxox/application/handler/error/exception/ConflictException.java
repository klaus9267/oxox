package kimandhong.oxox.application.handler.error.exception;

import kimandhong.oxox.application.handler.error.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends BaseException {
  public ConflictException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
