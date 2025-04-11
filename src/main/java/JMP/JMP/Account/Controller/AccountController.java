package JMP.JMP.Account.Controller;

import JMP.JMP.Account.Service.AccountService;
import JMP.JMP.Account.Dto.DtoRegister;
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

    //  회원가입
    @PostMapping("/register")
    public ResponseEntity<?> submitSignUpForm(@RequestBody DtoRegister dtoRegister) {

        ResponseEntity<?> response = accountService.register(dtoRegister);

        if(response.getStatusCode() == HttpStatus.CONFLICT){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이메일입니다.");
        }

        return response;
    }

}
