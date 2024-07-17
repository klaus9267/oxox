package kimandhong.oxox.auth;

import kimandhong.oxox.domain.User;
import kimandhong.oxox.handler.error.ErrorCode;
import kimandhong.oxox.handler.error.exception.ForbiddenException;
import kimandhong.oxox.handler.error.exception.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
  public Long getCustomUserId() {
    return this.getCurrentUser().getId();
  }

  public User getCurrentUser() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null) {
      throw new ForbiddenException(ErrorCode.NOT_FOUND_USER);
    }
    if (authentication.getPrincipal().equals("anonymousUser")) {
      throw new ForbiddenException(ErrorCode.LOGIN_REQUIRED);
    }

    return (User) authentication.getPrincipal();
  }

  public void loginCheck() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal().equals("anonymousUser")) {
      throw new NotFoundException(ErrorCode.LOGIN_REQUIRED);
    }
  }
}
