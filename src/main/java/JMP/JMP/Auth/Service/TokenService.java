package JMP.JMP.Auth.Service;

import JMP.JMP.Account.Entity.RefreshEntity;
import JMP.JMP.Account.Repository.RefreshRepository;
import JMP.JMP.Auth.Dto.LoginSuccessResponse;
import JMP.JMP.Auth.Dto.ReussueSuccessResponse;
import JMP.JMP.Auth.Security.JWTUtil;
import JMP.JMP.Enum.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static JMP.JMP.Auth.Service.util.TokenConst.*;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ResponseEntity<?> issueToken(String email, Role role, HttpServletResponse response) {

        String accessToken = jwtUtil.createJwt(TOKEN_TYPE_ACCESS, email, role.toString(), ACCESS_TOKEN_EXPIRATION);
        String refreshToken = jwtUtil.createJwt(TOKEN_TYPE_REFRESH, email, role.toString(), REFRESH_TOKEN_EXPIRATION);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Set-Cookie", "refresh=" + refreshToken + "; Path=/; HttpOnly; Secure; SameSite=None");

        // 만료된 토큰 삭제 후 적용 flush
        refreshRepository.findByUsername(email)
                .ifPresent(refreshEntity -> {
                    refreshRepository.delete(refreshEntity);
                    refreshRepository.flush();
                });

        // 토큰 새로 발급
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(email);
        refreshEntity.setRefresh(refreshToken);
        refreshEntity.setExpiration(
                ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                        .plus(REFRESH_TOKEN_EXPIRATION, ChronoUnit.SECONDS)
                        .toString()
        );

        ZonedDateTime accessTokenExpiresAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .plus(ACCESS_TOKEN_EXPIRATION, ChronoUnit.MILLIS);

        refreshRepository.save(refreshEntity);

        return ResponseEntity.ok(LoginSuccessResponse.of(200, "로그인 성공", role, ACCESS_TOKEN_EXPIRATION, accessTokenExpiresAt));
    }

    // access 토큰 만료시 Refresh Token 재발급
    @Transactional
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_TYPE_REFRESH.equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        if (refresh == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("refresh token 없음");
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh token 만료");
        }

        if (!TOKEN_TYPE_REFRESH.equals(jwtUtil.getCategory(refresh))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 refresh token");
        }

        if (!refreshRepository.existsByRefresh(refresh)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB에 존재하지 않는 refresh token");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        refreshRepository.findByUsername(username)
                .ifPresent(refreshEntity -> {
                    refreshRepository.delete(refreshEntity);
                    refreshRepository.flush();
                });

        String newAccess = jwtUtil.createJwt(TOKEN_TYPE_ACCESS, username, role, ACCESS_TOKEN_EXPIRATION);
        String newRefresh = jwtUtil.createJwt(TOKEN_TYPE_REFRESH, username, role, REFRESH_TOKEN_EXPIRATION);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(newRefresh);

        refreshEntity.setExpiration(
                ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                        .plus(REFRESH_TOKEN_EXPIRATION, ChronoUnit.SECONDS)
                        .toString()
        );

        ZonedDateTime accessTokenExpiresAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .plus(ACCESS_TOKEN_EXPIRATION, ChronoUnit.MILLIS);
        refreshRepository.save(refreshEntity);

//        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(createCookie(TOKEN_TYPE_REFRESH, newRefresh));

        return ResponseEntity.ok(ReussueSuccessResponse.of(200, "토큰 재발급 성공", newAccess, ACCESS_TOKEN_EXPIRATION, accessTokenExpiresAt));
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (REFRESH_TOKEN_EXPIRATION / 1000));
        return cookie;
    }

    public void logoutCookie(HttpServletResponse response){
        Cookie cookie = new Cookie(TOKEN_TYPE_REFRESH, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
