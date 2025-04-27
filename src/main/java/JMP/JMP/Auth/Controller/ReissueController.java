package JMP.JMP.Auth.Controller;

import JMP.JMP.Account.Entity.RefreshEntity;
import JMP.JMP.Account.Repository.RefreshRepository;
import JMP.JMP.Auth.Service.AuthService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

         ResponseEntity<?> result = authService.reissue(request, response);

         return result;
    }


}
