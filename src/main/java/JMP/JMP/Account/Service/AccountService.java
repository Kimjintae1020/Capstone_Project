package JMP.JMP.Account.Service;

import JMP.JMP.Account.Dto.DtoRegister;
import JMP.JMP.Account.Dto.ErrorResponse;
import JMP.JMP.Account.Dto.SuccessResponse;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Enum.Role;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Enum.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

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


}
