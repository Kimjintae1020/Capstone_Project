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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        log.info("JWT Filter URI: {}", requestUri);

        if (!requestUri.startsWith("/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader("Authorization");

        if (accessToken == null || accessToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring(7);

        log.info("access token: {}", accessToken);

        if (accessToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        try {
            log.info("access token: {}", accessToken);
            jwtUtil.isExpired(accessToken);

        } catch (ExpiredJwtException e) {
            System.out.println("[JWTFilter] Access Token 만료됨");

            if (refreshToken != null) {
                try {
                    jwtUtil.isExpired(refreshToken); // 만료 안되었으면 정상

                    String username = jwtUtil.getUsername(refreshToken);
                    String role = jwtUtil.getRole(refreshToken);

                    String newAccessToken = jwtUtil.createJwt("access", username, role, 15 * 60 * 1000L);

                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    accessToken = newAccessToken; // 새 토큰으로 교체

                } catch (ExpiredJwtException ex) {
                    System.out.println("[JWTFilter] Refresh Token 만료됨");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // access token 검증
        String category = jwtUtil.getCategory(accessToken);
        if (!"access".equals(category)) {
            System.out.println("[JWTFilter] access 토큰 아님");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

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

        filterChain.doFilter(request, response);
    }
}
