package kimandhong.oxox.handler.error.exception;

import kimandhong.oxox.handler.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {
  public NotFoundException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
