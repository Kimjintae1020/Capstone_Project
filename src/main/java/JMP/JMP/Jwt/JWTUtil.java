package JMP.JMP.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public String getCategory(String token) {
        return parseClaims(token).get("category", String.class);
    }


    // 토근이 소멸, 만료되었는지 확인하는 메소드
    public boolean isExpired(String token) {
        try {
            return parseClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // 토큰이 만료되었음을 true로 반환
        }
    }

    // 로그인이 성공했을때 토큰을 생성해서 응답해주는 메소드            expiredMs -> 토큰이 살아있을 시간
    public String createJwt(String category, String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
    // 토큰을 파싱하는 메서드 (중복 코드 제거)
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}