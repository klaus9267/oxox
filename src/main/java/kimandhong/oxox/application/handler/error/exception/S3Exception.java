package kimandhong.oxox.application.handler.error.exception;

import kimandhong.oxox.application.handler.error.ErrorCode;
import lombok.Getter;

@Getter
public class S3Exception extends BaseException {
  public S3Exception(final ErrorCode errorCode) {
    super(errorCode);
  }
}
