package kimandhong.oxox.application.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kimandhong.oxox.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  private static final String HEADER_STRING = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";

  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    final String authorizationHeader = request.getHeader(HEADER_STRING);

    if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
      final String token = authorizationHeader.substring(7);
      if (jwtUtil.validateToken(token)) {
        final Long userId = jwtUtil.getUserId(token);
        log.info("--------------------------------------- login(userId = " + userId + ") ---------------------------------------");
        userRepository.findById(userId).ifPresent(user -> {
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
              new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("USER")));
          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        });
        log.info("-------------------------------------------------------------------------------------------------");
      }
    }

    filterChain.doFilter(request, response);
  }
}
