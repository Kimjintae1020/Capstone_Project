package JMP.JMP.Apply.Service;

import JMP.JMP.Account.Dto.ErrorResponse;
import JMP.JMP.Account.Dto.SuccessResponse;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Apply.Entity.Apply;
import JMP.JMP.Apply.Repository.ApplyRepository;
import JMP.JMP.Enum.ApplyStatus;
import JMP.JMP.Enum.ErrorCode;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final JWTUtil jwtUtil;
    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public ResponseEntity<?> projectApply(String token, Long projectId) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);      // ex) loginId: company@gmail.com

        Project project = projectRepository.findById(projectId).orElse(null);
        Account account = accountRepository.findByEmail(loginId).orElse(null);

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
                .status(ApplyStatus.COMPLETED)
                .appliedAt(LocalDate.now())
                .build();
        applyRepository.save(savedApply);



        log.info("프로젝트 지원 성공");
        return ResponseEntity.ok(SuccessResponse.of(200, "프로젝트 지원 성공"));

    }
}
