package JMP.JMP.Account.Service;

import JMP.JMP.Account.Dto.DtoLogin;
import JMP.JMP.Account.Dto.DtoRegister;
import JMP.JMP.Account.Dto.ErrorResponse;
import JMP.JMP.Account.Dto.SuccessResponse;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Entity.RefreshEntity;
import JMP.JMP.Account.Role.Role;
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
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    // 회원가입로직 (사용자)
    public ResponseEntity<?> register(DtoRegister dtoRegister) {

        // 이메일 중복검사
        if (accountRepository.existsByEmail(dtoRegister.getEmail())) {
            log.info("이메일 중복");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.DUPLICATE_EMAIL));
        }

        // 핸드폰 중복검사
        if (accountRepository.existsByPhone(dtoRegister.getPhone())) {
            log.info("핸드폰번호 중복");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.DUPLICATE_PHONE));
        }


        String encodedPassword = passwordEncoder.encode(dtoRegister.getPassword());

            Account savedaccount = Account.builder()
                    .email(dtoRegister.getEmail())
                    .password(encodedPassword)
                    .name(dtoRegister.getName())
                    .birthYear(dtoRegister.getBirthYear())
                    .gender(dtoRegister.getGender())
                    .phone(dtoRegister.getPhone())
                    .major(dtoRegister.getMajor())
                    .education(dtoRegister.getEducation())
                    .role(Role.USER)
                    .build();
            accountRepository.save(savedaccount);

            // 회원가입 성공 Http 상태코드 (201 created)
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(201, "회원가입 완료"));

    }


    public ResponseEntity<?> login(DtoLogin dtoLogin, HttpServletResponse response) {

        Optional<Account> optionalAccount = accountRepository.findByEmail(dtoLogin.getEmail());

        // 해당 이메일이 DB에 없을 때
        if (optionalAccount.isEmpty()) {
            log.info("해당 이메일에 DB가 없음");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_FOUND));
        }
        // Optional로 감싸진거 객체로 풀음
        Account account = optionalAccount.get();

        // 비밀번호가 일치하지 않을 때
        if (!passwordEncoder.matches(dtoLogin.getPassword(), account.getPassword())) {
            log.info("비밀번호가 일치하지 않음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.INVALID_PASSWORD));
        }

        // Access Token, Refress Token 발급
        String accessToken = jwtUtil.createJwt("access", account.getEmail(), account.getRole().toString(), 600000L);
        String refreshToken = jwtUtil.createJwt("refresh", account.getEmail(), account.getRole().toString(), 86400000L);

        log.info("accessToken: " + accessToken);    // 얘는 프론트에 보내서 관리
        log.info("refreshToken: " + refreshToken);  // 얘는 백엔드에서 관리해야함


        // Refresh Token 저장
        Optional<RefreshEntity> existingToken = refreshRepository.findByUsername(account.getEmail());

        // 기존 데이터가 있으면 업데이트
        if (existingToken.isPresent()) {
            RefreshEntity refreshEntity = existingToken.get();
            refreshEntity.setRefresh(refreshToken);
            refreshEntity.setExpiration("86400000");
            refreshRepository.save(refreshEntity);
        }
        // 기존 데이터가 없어 새로 저장
        else {
            refreshRepository.save(new RefreshEntity(account.getEmail(), refreshToken, "86400000"));
        }

        // 프론트 보낼 accessToken 헤더에 저장
        response.setHeader("Authorization", "Bearer " + accessToken);
        // 쿠키에 refreshToken 저장
        response.setHeader("Set-Cookie", "refresh=" + refreshToken + "; Path=/; HttpOnly; Secure; SameSite=None");

        log.info("로그인한 사용자: " + dtoLogin.getEmail());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.of(200, "로그인 성공"));

    }

}
