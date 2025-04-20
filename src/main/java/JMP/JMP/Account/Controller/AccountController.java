package JMP.JMP.Account.Controller;

import JMP.JMP.Account.Dto.DtoLogin;
import JMP.JMP.Account.Dto.ErrorResponse;
import JMP.JMP.Account.Service.AccountService;
import JMP.JMP.Account.Dto.DtoRegister;
import JMP.JMP.Account.Service.AuthService;
import JMP.JMP.Enum.ErrorCode;
import JMP.JMP.Enum.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final AuthService authService;

    //  회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DtoRegister dtoRegister) {

        ResponseEntity<?> response = accountService.register(dtoRegister);

        return response;
    }

    // 로그인 로직
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DtoLogin dtoLogin, HttpServletResponse response) {

        // 사용자 로그인
        if (dtoLogin.getRole() == Role.USER) {
            return authService.loginUser(dtoLogin, response);
        }
        // 기업 담당자 로그인
        else if (dtoLogin.getRole() == Role.PENDING || dtoLogin.getRole() == Role.COMPANY) {
            return authService.loginCompany(dtoLogin, response);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.INVALID_ROLE));

    }
}


