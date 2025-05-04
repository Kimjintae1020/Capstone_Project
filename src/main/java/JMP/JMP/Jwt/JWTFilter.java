package JMP.JMP.Jwt;

import JMP.JMP.Account.Dto.CustomUserDetails;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Enum.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        log.info("JWT Filter URI: {}", requestUri);

        if (requestUri.equals("/api/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractAccessToken(request);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = extractRefreshToken(request);

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            if (refreshToken != null) {
                try {
                    jwtUtil.isExpired(refreshToken);

                    String username = jwtUtil.getUsername(refreshToken);
                    String role = jwtUtil.getRole(refreshToken);
                    String newAccessToken = jwtUtil.createJwt("access", username, role, 15 * 60 * 1000L);
                    response.setHeader("Authorization", "Bearer " + newAccessToken);

                    // 인증 객체 설정
                    setAuthentication(username, role);

                    filterChain.doFilter(request, response);
                    return;

                } catch (ExpiredJwtException ex) {
                    log.info("RefreshToken도 만료됨");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!"access".equals(category)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        setAuthentication(username, role);

        filterChain.doFilter(request, response);

    }

    private String extractAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null || !accessToken.startsWith("Bearer ")) return null;
        return accessToken.substring(7);
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void setAuthentication(String username, String role) {
        Account account = new Account();
        account.setEmail(username);
        account.setRole(Role.valueOf(role));
        CustomUserDetails customUserDetails = new CustomUserDetails(account);

        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
