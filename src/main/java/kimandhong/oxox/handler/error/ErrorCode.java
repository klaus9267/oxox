package kimandhong.oxox.handler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request."),
  BAD_REQUEST_LOGIN(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호를 확인해주세요"),
  WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 확인해주세요."),

  UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "Unauthorized."),
  FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "Forbidden."),

  NOT_FOUND(HttpStatus.NOT_FOUND, "Not found."),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  NOT_FOUND_POLL(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Not allowed method."),

  CONFLICT(HttpStatus.CONFLICT, "Conflict"),
  CONFLICT_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다."),
  CONFLICT_GOOGLE(HttpStatus.CONFLICT, "Google로 가입된 이메일입니다."),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error."),
  S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "s3 upload failed"),
  S3_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "s3 delete failed"),
  POLL_CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Poll creation failed.");

  private final HttpStatus httpStatus;
  private final String message;
}
