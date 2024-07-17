package kimandhong.oxox.handler.error.exception;

import kimandhong.oxox.handler.error.ErrorCode;

public class ForbiddenException extends BaseException {
  public ForbiddenException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
