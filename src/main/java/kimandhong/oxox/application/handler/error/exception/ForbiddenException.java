package kimandhong.oxox.application.handler.error.exception;

import kimandhong.oxox.application.handler.error.ErrorCode;

public class ForbiddenException extends BaseException {
  public ForbiddenException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
