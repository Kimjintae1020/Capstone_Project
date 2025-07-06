package JMP.JMP.Admin.Controller;

import JMP.JMP.Admin.Service.AdminService;
import JMP.JMP.Enum.PostRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminCompanyController {

    private final AdminService adminService;

    //  기업담당자 회원가입 승인 대기 목록 조회
    @GetMapping("/companies/pending")
    public ResponseEntity<?> getCompanyRegisterList(@RequestHeader(value = "Authorization", required = false) String token){

        ResponseEntity<?> response = adminService.getCompanyRegisterList(token);

        return response;
    }

    // 기업 담당자 회원가입 승인
    @PatchMapping("/companies/{companyId}/approve")
    public ResponseEntity<?> updateCompanyPostStatus(@RequestHeader(value = "Authorization", required = false) String token,
                                                     @PathVariable Long companyId,
                                                     @RequestParam("status") PostRole status){

        ResponseEntity<?> response = adminService.updateCompanyPostStatus(token, companyId, status);

        return response;
    }

}
