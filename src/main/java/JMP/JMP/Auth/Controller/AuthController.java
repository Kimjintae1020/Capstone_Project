package JMP.JMP.Auth.Controller;

import JMP.JMP.Auth.Dto.DtoLogin;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Auth.Service.AuthService;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Enum.Role;
import JMP.JMP.Jwt.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    // 로그인 로직
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DtoLogin dtoLogin, HttpServletResponse response) {

        // 사용자 로그인
        if (dtoLogin.getRole() == Role.USER) {
            return authService.loginUser(dtoLogin, response);
        }
        // 기업 담당자 로그인
        else if (dtoLogin.getRole() == Role.COMPANY) {
            return authService.loginCompany(dtoLogin, response);
        }

        log.info("check reissue");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.INVALID_ROLE));

    }
    // 이메일 중복 검사
    @GetMapping("/email/exist")
    public ResponseEntity<?> existEmailCheck(@RequestParam String email) {
        return authService.existEmailCheck(email);
    }

    // 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseEntity<?> getMypage(@RequestHeader(value = "Authorization", required = false) String token){

        // 기업 또는 사용자별 마이페이지 제공해야하는가?
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);
        Role role = Role.valueOf(jwtUtil.getRole(accessToken));

        ResponseEntity<?> response = authService.getMypage(loginId, role);
        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token, HttpServletResponse response) {
        authService.logout(token, response);
        return ResponseEntity.ok(SuccessResponse.of(200, "로그아웃 성공"));
    }

}

