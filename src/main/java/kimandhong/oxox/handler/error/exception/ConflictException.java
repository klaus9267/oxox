package kimandhong.oxox.handler.error.exception;

import kimandhong.oxox.handler.error.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends BaseException {
  public ConflictException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
