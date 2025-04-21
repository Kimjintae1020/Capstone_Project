package JMP.JMP.Auth.Controller;

import JMP.JMP.Account.Dto.DtoLogin;
import JMP.JMP.Account.Dto.ErrorResponse;
import JMP.JMP.Auth.DtoEmailRequest;
import JMP.JMP.Auth.Service.AuthService;
import JMP.JMP.Enum.ErrorCode;
import JMP.JMP.Enum.Role;
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.INVALID_ROLE));

    }
    // 이메일 중복 검사
    @GetMapping("/email/exist")
    public ResponseEntity<?> existEmailCheck(@RequestParam String email) {
        return authService.existEmailCheck(email);
    }
}
