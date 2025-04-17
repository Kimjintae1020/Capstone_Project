package JMP.JMP.Company.Controller;

import JMP.JMP.Company.Service.CompanyService;
import JMP.JMP.Company.Dto.DtoCompanyRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CompanyController {

    private final CompanyService companyService;


    //  회원가입 (기업 담당자)
    @PostMapping("/register/company")
    public ResponseEntity<?> register(@RequestBody DtoCompanyRegister dtoCompanyRegister) {

        ResponseEntity<?> response = companyService.registerCompany(dtoCompanyRegister);

        return response;
    }

}
