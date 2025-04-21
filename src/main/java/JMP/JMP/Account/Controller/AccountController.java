package JMP.JMP.Account.Controller;

import JMP.JMP.Account.Service.AccountService;
import JMP.JMP.Account.Dto.DtoRegister;
import JMP.JMP.Auth.Service.AuthService;
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
    private final AuthService authService;

    //  회원가입
    @PostMapping("/register/account")
    public ResponseEntity<?> register(@RequestBody DtoRegister dtoRegister) {

        ResponseEntity<?> response = accountService.register(dtoRegister);

        return response;
    }


}


