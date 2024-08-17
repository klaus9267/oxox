package kimandhong.oxox.application.handler.error.exception;

import kimandhong.oxox.application.handler.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {
  public NotFoundException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
