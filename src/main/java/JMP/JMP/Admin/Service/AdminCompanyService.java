package JMP.JMP.Admin.Service;

import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Admin.Dto.DtoPendingCompanyList;
import JMP.JMP.Company.Repository.CompanyRepository;
import JMP.JMP.Enum.PostRole;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminCompanyService {

    private final JWTUtil jwtUtil;
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;


    //  기업담당자 회원가입 승인 대기 목록 조회
    public ResponseEntity<?> getCompanyRegisterList(String token) {

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        boolean isAdmin = accountRepository.findByEmail(loginId)
                .map(account -> account.getRole().name().equals("ADMIN"))
                .orElse(false);

        // 관리자가 아닐시 접근제어
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of(ErrorCode.INVALID_ADMIN_ROLE));
        }

        List<DtoPendingCompanyList> response = companyRepository.findAllPendingCompany(PostRole.PENDING);

        return ResponseEntity.ok(response);
    }
}