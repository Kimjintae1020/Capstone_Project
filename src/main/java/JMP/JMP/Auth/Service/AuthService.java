package JMP.JMP.Auth.Service;

import JMP.JMP.Account.Dto.DtoLogin;
import JMP.JMP.Account.Dto.ErrorResponse;
import JMP.JMP.Account.Dto.SuccessResponse;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Entity.RefreshEntity;
import JMP.JMP.Auth.DtoEmailRequest;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Company.Repository.CompanyRespository;
import JMP.JMP.Enum.Role;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Account.Repository.RefreshRepository;
import JMP.JMP.Enum.ErrorCode;
import JMP.JMP.Jwt.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final AccountRepository accountRepository;
    private final CompanyRespository companyRespository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    private static final long ACCESS_TOKEN_VALIDITY = 600000L; // 10분
    private static final long REFRESH_TOKEN_VALIDITY = 86400000L; // 24시간


    // 사용자 로그인
    public ResponseEntity<?> loginUser(DtoLogin dto, HttpServletResponse response) {
        Optional<Account> optional = accountRepository.findByEmail(dto.getEmail());
        if (optional.isEmpty()) {
            return errorResponse(ErrorCode.EMAIL_NOT_FOUND);
        }

        Account account = optional.get();   // Optional 꺼내기


        // 비밀번호가 일치하지 않으면
        if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
            return errorResponse(ErrorCode.INVALID_PASSWORD);
        }
        
        return issueToken(account.getEmail(), account.getRole(), response);
    }


    // 기업 당당자 로그인
    public ResponseEntity<?> loginCompany(DtoLogin dto, HttpServletResponse response) {

        Optional<Company> optional = companyRespository.findByEmail(dto.getEmail());

        if (optional.isEmpty()) {
            return errorResponse(ErrorCode.EMAIL_NOT_FOUND);
        }
        Company company = optional.get();
        if (!passwordEncoder.matches(dto.getPassword(), company.getPassword())) {
            return errorResponse(ErrorCode.INVALID_PASSWORD);
        }
        return issueToken(company.getEmail(), company.getRole(), response);
    }

    private ResponseEntity<?> issueToken(String email, Role role, HttpServletResponse response) {

        String accessToken = jwtUtil.createJwt("access", email, role.toString(), ACCESS_TOKEN_VALIDITY);
        String refreshToken = jwtUtil.createJwt("refresh", email, role.toString(), REFRESH_TOKEN_VALIDITY);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Set-Cookie", "refresh=" + refreshToken + "; Path=/; HttpOnly; Secure; SameSite=None");

        // Refresh 저장
        refreshRepository.save(
                refreshRepository.findByUsername(email)
                        .map(r -> { r.setRefresh(refreshToken); return r; })
                        .orElse(new RefreshEntity(email, refreshToken, "86400000L"))
        );

        return ResponseEntity.ok(SuccessResponse.of(200, "로그인 성공"));
    }

    private ResponseEntity<?> errorResponse(ErrorCode errorCode) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(errorCode));
    }

    // 이메일 중복 검사
    public ResponseEntity<?> existEmailCheck(String email) {
        if (accountRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.DUPLICATE_EMAIL));
        }
        if (companyRespository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.DUPLICATE_EMAIL));
        }

        return ResponseEntity.ok(SuccessResponse.of(200, "사용 가능한 이메일 입니다."));

    }
}
