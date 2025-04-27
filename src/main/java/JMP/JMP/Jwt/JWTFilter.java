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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class JWTFilter  extends OncePerRequestFilter {


    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestUri = request.getRequestURI();

        if (requestUri.equals("/api/login") || requestUri.equals("/api/reissue") || requestUri.equals("/api/signup")) {
            filterChain.doFilter(request, response);
            return;
        }


        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("Authorization");

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7); // "Bearer " 제거
        }

        // 쿠키에서 refresh 토큰 찾기
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


        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request, response);

            return;
        }

        try {
            // 액세스 토큰의 만료 여부를 확인
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            System.out.println("[JWTFilter] Access Token 만료됨");

            // 리프레시 토큰이 유효한 경우 새로운 액세스 토큰 발급
            if (refreshToken != null && !jwtUtil.isExpired(refreshToken)) {
                // 리프레시 토큰으로 사용자 정보를 가져옴
                String username = jwtUtil.getUsername(refreshToken);
                String role = jwtUtil.getRole(refreshToken);

                // 새로운 액세스 토큰 생성
                String newAccessToken = jwtUtil.createJwt("access", username, role, 15 * 60 * 1000L); // 액세스 토큰 15분 유효

                // 새 액세스 토큰을 응답 헤더에 추가
                response.setHeader("Authorization", "Bearer " + newAccessToken);
                accessToken = newAccessToken; // 새로운 토큰으로 교체
            } else {
                // 리프레시 토큰도 만료된 경우
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }


        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!"access".equals(category)) {

            //response body
            System.out.println("[JWTFilter] access 토큰 아님");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값을 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);


        Account account = new Account();
        account.setEmail(username);
        account.setRole(Role.valueOf(role));

        CustomUserDetails customUserDetails = new CustomUserDetails(account);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }

}