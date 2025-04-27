package JMP.JMP.Auth.Controller;

import JMP.JMP.Account.Entity.RefreshEntity;
import JMP.JMP.Account.Repository.RefreshRepository;
import JMP.JMP.Jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        if (refresh == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("refresh token 없음");
        }

        // 2. refresh token 검증
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh token 만료");
        }

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 refresh token");
        }

        if (!refreshRepository.existsByRefresh(refresh)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB에 존재하지 않는 refresh token");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String newAccessToken = jwtUtil.createJwt("access", username, role, 600000L); // 10분짜리 access token

        String newRefreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L); // 하루짜리 refresh token
        refreshRepository.deleteByRefresh(refresh);
        refreshRepository.save(new RefreshEntity(username, newRefreshToken, new Date(System.currentTimeMillis() + 86400000L).toString()));

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(createCookie("refresh", newRefreshToken));

        return ResponseEntity.ok("토큰 재발급 성공");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        return cookie;
    }
}
