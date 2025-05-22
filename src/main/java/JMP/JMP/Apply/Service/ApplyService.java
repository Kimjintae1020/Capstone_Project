package JMP.JMP.Apply.Service;

import JMP.JMP.Company.Repository.CompanyRepository;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Apply.Entity.Apply;
import JMP.JMP.Apply.Repository.ApplyRepository;
import JMP.JMP.Enum.ApplyStatus;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Repository.ProjectRepository;
import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final JWTUtil jwtUtil;
    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public ResponseEntity<?> projectApply(String token, Long projectId, Long resumeId) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);      // ex) loginId: company@gmail.com

        Project project = projectRepository.findById(projectId).orElse(null);
        Account account = accountRepository.findByEmail(loginId).orElse(null);
        Resume resume = resumeRepository.findById(resumeId).orElse(null);

        // 프로젝트 시작일이 오늘보다 이전이면 (이미 사작이라면?)
        if (project.getStartDate().isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of(ErrorCode.APPLICATION_CLOSED));
        }

        // 지원 중복 오류
        if (applyRepository.existsByAccountAndProject(account, project)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of(ErrorCode.ALREADY_APPLIED));
        }

        Apply savedApply = Apply.builder()
                .project(project)
                .account(account)
                .resume(resume)
                .status(ApplyStatus.PENDING)
                .appliedAt(LocalDate.now())
                .build();
        applyRepository.save(savedApply);


        log.info("프로젝트 지원 성공");
        return ResponseEntity.ok(SuccessResponse.of(200, "프로젝트 지원 성공"));

    }

    // 프로젝트 지원자 선정
    @Transactional
    public ResponseEntity<?> updateApplyStatus(String token, Long projectId, Long applyId, ApplyStatus status) {

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        log.info(loginId);
        boolean checkCompany = companyRepository.existsByEmail(loginId);

        // 기업 담당자만 접근 가능
        if (!checkCompany) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.UNAUTHORIZED_ACCESS));
        }

        Optional<Apply> findApplyId = applyRepository.findById(applyId);
        Apply apply = findApplyId.get();

        if (findApplyId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of(ErrorCode.APPLY_NOT_FOUND));
        }

        // 프로젝트 일치 여부 확인
        if (!findApplyId.get().getProject().getProjectId().equals(projectId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of(ErrorCode.INVALID_ACCESS));
        }

        // 중복 상태 요청 방지
        if (apply.getStatus() == status) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of(ErrorCode.ALREADY_SET_STATUS));
        }

        // 합격처리
        if (status == ApplyStatus.ACCEPTED) {

            apply.setStatus(status);

            return ResponseEntity.ok(SuccessResponse.of(200, loginId + "님 합격처리 되었습니다."));
        }

        // 불합격처리
        else if (status == ApplyStatus.REJECTED) {

            apply.setStatus(status);

            return ResponseEntity.ok(SuccessResponse.of(200, loginId + "님 불합격처리 되었습니다."));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.INVALID_STATUS));
    }
}
