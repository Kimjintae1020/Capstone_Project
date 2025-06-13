package JMP.JMP.Apply.Service;

import JMP.JMP.Apply.Dto.DtoAppliedProject;
import JMP.JMP.Company.Repository.CompanyRepository;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Apply.Entity.Apply;
import JMP.JMP.Apply.Repository.ApplyRepository;
import JMP.JMP.Enum.ApplyStatus;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.Exception.CustomException;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Repository.ProjectRepository;
import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Repository.ResumeRepository;
import JMP.JMP.SSE.EventPayload;
import JMP.JMP.SSE.SSEService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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
    private final SSEService SSEService;

    // 프로젝트 지원
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
            log.info("프로젝트 시작일이 오늘보다 이전");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of(ErrorCode.APPLICATION_CLOSED));
        }

        // 지원 중복 오류
        if (applyRepository.existsByAccountAndProject(account, project)) {
            log.info("이미 지원한 공고입니다.");
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


        // SSE 기반 알림  지원자 -> 프로젝트 작성자  message: "[지원자 이름] 님이 프로젝트에 지원하셨습니다."
        String senderName = account.getName(); // 지원자 이름
        Long receiverId = project.getManager().getId(); // 프로젝트 작성자
        String role = project.getManager().getRole().name();


        EventPayload payload = new EventPayload(
                "apply",
                senderName + " 님이 '" + project.getTitle() + "' 프로젝트에 지원하셨습니다.",
                role,
                receiverId,
                senderName,
                LocalDate.now().toString()
        );

        // 알림 전송
        SSEService.broadcast(receiverId, payload);
        log.info("상대방 역할 " + role);

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

    // 지원한 프로젝트 공고 조회
    @Transactional(readOnly = true)
    public List<DtoAppliedProject> getProjectsApplied(String token) {

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        Account account = accountRepository.findByEmail(loginId).orElse(null);

        List<Apply> applyList =  applyRepository.findByAccountId(account.getId());

        return applyList.stream()
                .map(apply -> {
                    Project project = apply.getProject();

                    // 진행 상태 계산 (프론트에서 할 거면 날짜만 전달)
                    LocalDate today = LocalDate.now();
                    String status = "모집중";
                    if (project.getRecruitDeadline() != null) {
                        if (project.getRecruitDeadline().isBefore(today)) {
                            status = "모집마감";
                        } else if (project.getRecruitDeadline().minusDays(3).isBefore(today)) {
                            status = "마감임박";
                        }
                    }

                    return new DtoAppliedProject(
                            project.getProjectId(),
                            project.getTitle(),
                            project.getRequiredSkill(),
                            project.getRecruitDeadline(),
                            project.getViewCount(),
                            apply.getAppliedAt()
                    );
                })
                .toList();
    }
}
