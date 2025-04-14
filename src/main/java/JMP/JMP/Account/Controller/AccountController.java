package JMP.JMP.Account.Controller;

import JMP.JMP.Account.Dto.DtoLogin;
import JMP.JMP.Account.Service.AccountService;
import JMP.JMP.Account.Dto.DtoRegister;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    //  회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DtoRegister dtoRegister) {

        ResponseEntity<?> response = accountService.register(dtoRegister);

        return response;
    }

    // 로그인 로직
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DtoLogin dtoLogin, HttpServletResponse response) {

        ResponseEntity<?> loginResponse = accountService.login(dtoLogin, response);


        return loginResponse;
    }

}
