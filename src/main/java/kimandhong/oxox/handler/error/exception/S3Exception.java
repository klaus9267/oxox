package kimandhong.oxox.handler.error.exception;

import kimandhong.oxox.handler.error.ErrorCode;
import lombok.Getter;

@Getter
public class S3Exception extends BaseException {
  public S3Exception(final ErrorCode errorCode) {
    super(errorCode);
  }
}
