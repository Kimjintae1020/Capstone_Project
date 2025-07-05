package JMP.JMP.Auth.Service;

import JMP.JMP.Auth.Dto.*;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Entity.RefreshEntity;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Company.Repository.CompanyRepository;
import JMP.JMP.Enum.Role;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Account.Repository.RefreshRepository;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.Exception.CustomException;
import JMP.JMP.Auth.Security.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static JMP.JMP.Auth.Service.util.TokenConst.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final TokenService tokenService;


    // 사용자 로그인
    public ResponseEntity<?> loginUser(DtoLogin dto, HttpServletResponse response) {
        Account account = accountRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 비밀번호가 일치하지 않으면
        if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.INVALID_PASSWORD));
        }

        return tokenService.issueToken(account.getEmail(), account.getRole(), response);
    }


    // 기업 당당자 로그인
    public ResponseEntity<?> loginCompany(DtoLogin dto, HttpServletResponse response) {

        Optional<Company> optional = companyRepository.findByEmail(dto.getEmail());

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_FOUND));
        }
        Company company = optional.get();
        if (!passwordEncoder.matches(dto.getPassword(), company.getPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.INVALID_PASSWORD));
        }
        return tokenService.issueToken(company.getEmail(), company.getRole(), response);
    }

    // 이메일 중복 검사
    public ResponseEntity<?> existEmailCheck(String email) {
        if (accountRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.DUPLICATE_EMAIL));
        }
        if (companyRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.DUPLICATE_EMAIL));
        }

        return ResponseEntity.ok(SuccessResponse.of(200, "사용 가능한 이메일 입니다."));

    }

    // 마이페이지 조회
    public ResponseEntity<?> getMypage(String loginId, Role role) {

        if (role == Role.USER) {
            Optional<Account> optionalAccount = accountRepository.findByEmail(loginId);

            if (optionalAccount.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_FOUND));
            }

            Account account = optionalAccount.get();
            DtoMypageAccount dto = new DtoMypageAccount(account);

            return ResponseEntity.ok(dto);

        }
        else if (role == Role.COMPANY) {
            Optional<Company> optionalCompany = companyRepository.findByEmail(loginId);

            if (optionalCompany.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_FOUND));
            }

            Company company = optionalCompany.get();
            DtoMypageCompany dto = new DtoMypageCompany(company);

            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.INVALID_ROLE));

    }

    @Transactional
    public void logout(String token, HttpServletResponse response) {

        String accessToken = token.replace("Bearer ", "");
        String email = jwtUtil.getUsername(accessToken);

        refreshRepository.findByUsername(email)
                .ifPresent(refreshRepository::delete);

        tokenService.logoutCookie(response);
    }
}