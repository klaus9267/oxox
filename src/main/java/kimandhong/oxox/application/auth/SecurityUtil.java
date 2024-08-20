package kimandhong.oxox.application.auth;

import kimandhong.oxox.application.handler.error.ErrorCode;
import kimandhong.oxox.application.handler.error.exception.ForbiddenException;
import kimandhong.oxox.domain.user.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
  public Long getCustomUserId() {
    return isLogin() ? this.getCurrentUser().getId() : null;
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

  public boolean isLogin() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return !authentication.getPrincipal().equals("anonymousUser");
  }
}
