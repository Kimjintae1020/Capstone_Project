package JMP.JMP.Admin.Controller;

import JMP.JMP.Admin.Service.AdminCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminCompanyController {

    private final AdminCompanyService adminCompanyService;

    //  기업담당자 회원가입 승인 대기 목록 조회
    @GetMapping("/admin/companies/pending")
    public ResponseEntity<?> getCompanyRegisterList(@RequestHeader(value = "Authorization", required = false) String token){

        ResponseEntity<?> response = adminCompanyService.getCompanyRegisterList(token);

        return response;
    }

}
