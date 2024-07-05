package kimandhong.oxox.handler.error;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
  private final LocalDateTime timestamp = LocalDateTime.now();
  private final int statusCode;
  private final String error;
  private final String message;

  public ErrorResponse(final ErrorCode errorCode) {
    this.statusCode = errorCode.getHttpStatus().value();
    this.error = errorCode.getHttpStatus().name();
    this.message = errorCode.getMessage();
  }

  public ErrorResponse(final ErrorCode errorCode, final String message) {
    this.statusCode = errorCode.getHttpStatus().value();
    this.error = errorCode.getHttpStatus().name();
    this.message = message;
  }
}
