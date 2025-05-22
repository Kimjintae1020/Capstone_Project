package JMP.JMP.Company.Service;

import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Enum.PostRole;
import JMP.JMP.Enum.Role;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Company.Dto.DtoCompanyRegister;
import JMP.JMP.Company.Repository.CompanyRepository;
import JMP.JMP.Error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입 (기업 담당자)
    public ResponseEntity<?> registerCompany(DtoCompanyRegister dtoCompanyRegister) {

        // 이메일 중복검사
        if (companyRepository.existsByEmail(dtoCompanyRegister.getEmail())) {
            log.info("기업 담당자 이메일 중복");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.DUPLICATE_COMPANY_EMAIL));
        }

        // 사업자 번호 중복 검사
        if (companyRepository.existsByBusinessNumber(dtoCompanyRegister.getBusinessNumber())) {
            log.info("사업자 번호 중복");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.DUPLICATE_BUSSINESS_NUMBER));
        }

        // 핸드폰 중복검사
        if (companyRepository.existsByPhone(dtoCompanyRegister.getPhone())) {
            log.info("핸드폰번호 중복");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.of(ErrorCode.DUPLICATE_COMPANY_PHONE));
        }

        String encodedPassword = passwordEncoder.encode(dtoCompanyRegister.getPassword());

        Company savedCompany = Company.builder()
                .email(dtoCompanyRegister.getEmail())
                .password(encodedPassword)
                .name(dtoCompanyRegister.getName())
                .gender(dtoCompanyRegister.getGender())
                .phone(dtoCompanyRegister.getPhone())
                .businessNumber(dtoCompanyRegister.getBusinessNumber())
                .position(dtoCompanyRegister.getPosition())
                .companyLocation(dtoCompanyRegister.getCompanyLocation())
                .companyName(dtoCompanyRegister.getCompanyName())
                .postRole(PostRole.PENDING)
                .role(Role.COMPANY)
                .build();
        companyRepository.save(savedCompany);

        log.info("기업 담당자 회원가입 완료");

        // 회원가입 성공 Http 상태코드 (201 created)
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(201, "회원가입 완료"));

    }
}
