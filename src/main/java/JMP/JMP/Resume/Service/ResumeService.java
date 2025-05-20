package JMP.JMP.Resume.Service;

import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Resume.Dto.*;
import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Entity.ResumeProject;
import JMP.JMP.Resume.Repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final AccountRepository accountRepository;
    private final JWTUtil jwtUtil;

    // 이력서 등록
    @Transactional
    public ResponseEntity<?> createResume(String token, DtoCreateResume dtoCreateResume, String savedPath) {

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        Optional<Account> accountOptional = accountRepository.findByEmail(loginId);

        if (accountOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_FOUND));
        }

        Account account = accountOptional.get();

        log.info(dtoCreateResume.getTitle());

        Resume resume = new Resume();
        resume.setAccount(account);
        resume.setTitle(dtoCreateResume.getTitle());
        resume.setIntro(dtoCreateResume.getIntro());
        resume.setSkills(dtoCreateResume.getSkills());
        resume.setGithuburl(dtoCreateResume.getGithubUrl());
        resume.setVisible(dtoCreateResume.isVisible());
        resume.setDevposition(dtoCreateResume.getDevposition());

        resume.setPhoto(savedPath);

        resume.setIntroduce(dtoCreateResume.getIntroduce());

        List<ResumeProject> resumeProjects = dtoCreateResume.getProjects().stream()
                .map(dtoProject -> ResumeProject.builder()
                        .name(dtoProject.getName())
                        .description(dtoProject.getDescription())
                        .techStack(dtoProject.getTechStack())
                        .githubLink(dtoProject.getGithubLink())
                        .resume(resume)
                        .build())
                .collect(Collectors.toList());

        resume.setProjects(resumeProjects);

        resume.setCreatedAt(LocalDate.now());
        resume.setUpdatedAt(LocalDate.now());

        resumeRepository.save(resume);

        return ResponseEntity.ok(CreateResumeSuccessResponse.of(201, "이력서 작성이 완료되었습니다.", resume.getResumeId()));
    }

    //  이력서 목록 조회
    @Transactional
    public List<DtoResumeList> getResumeList(String email) {

        Optional<Account> accountOptional = accountRepository.findByEmail(email);

        Account account = accountOptional.get();

        List<DtoResumeList> resumeList = resumeRepository.findAllResumes(account.getId());

        return resumeList.stream()
                .map(resume -> new DtoResumeList(
                        resume.getResumeId(),
                        resume.getTitle(),
                        resume.getIntro(),
                        resume.isVisible(),
                        resume.getCreatedAt(),
                        resume.getUpdatedAt()
                ))
                .toList();

    }

    // 이력서 삭제
    @Transactional
    public ResponseEntity<?> deleteResume(String email, Long resumeId) {

        Optional<Account> optionalAccount = accountRepository.findByEmail(email);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of(ErrorCode.RESUME_NOT_FOUND));
        }

        Account account = optionalAccount.get();
        Optional<Resume> resumeOptional = resumeRepository.findByResumeIdAndAccountId(resumeId, account.getId());

        resumeRepository.delete(resumeOptional.get());

        return ResponseEntity.ok().body("이력서가 성공적으로 삭제되었습니다.");
    }

    // 이력서 수정 기능
    @Transactional
    public ResponseEntity<?> updateResume(String email, Long resumeId, DtoUpdateResume dtoUpdateResume, String photo) {

        Optional<Account> optionalAccount = accountRepository.findByEmail(email);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_FOUND));
        }

        Account account = optionalAccount.get();

        Optional<Resume> optionalResume = resumeRepository.findByResumeIdAndAccountId(resumeId, account.getId());

        if (optionalResume.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of(ErrorCode.RESUME_NOT_OWNED));
        }

        Resume resume = new Resume();
        resume.setAccount(account);
        resume.setTitle(dtoUpdateResume.getTitle());
        resume.setIntro(dtoUpdateResume.getIntro());
        resume.setSkills(dtoUpdateResume.getSkills());
        resume.setGithuburl(dtoUpdateResume.getGithubUrl());
        resume.setVisible(dtoUpdateResume.isVisible());
        resume.setDevposition(dtoUpdateResume.getDevposition());
        resume.setPhoto(photo);
        resume.setIntroduce(dtoUpdateResume.getIntroduce());

        List<ResumeProject> resumeProjects = dtoUpdateResume.getProjects().stream()
                .map(dtoProject -> ResumeProject.builder()
                        .name(dtoProject.getName())
                        .description(dtoProject.getDescription())
                        .techStack(dtoProject.getTechStack())
                        .githubLink(dtoProject.getGithubLink())
                        .resume(resume)
                        .build())
                .collect(Collectors.toList());

        resume.setProjects(resumeProjects);

        resume.setCreatedAt(LocalDate.now());
        resume.setUpdatedAt(LocalDate.now());

        resumeRepository.save(resume);
        return ResponseEntity.ok(SuccessResponse.of(200, "이력서가 성공적으로 수정되었습니다."));
    }

    // 이력서 공개범위 수정
    @Transactional
    public ResponseEntity<?> updateResumevisible(String email, Long resumeId, boolean visible) {

        Optional<Account> optionalAccount = accountRepository.findByEmail(email);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_FOUND));
        }

        Account account = optionalAccount.get();

        Optional<Resume> optionalResume = resumeRepository.findByResumeIdAndAccountId(resumeId, account.getId());

        if (optionalResume.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponse.of(ErrorCode.RESUME_NOT_OWNED));
        }

        Resume resume = optionalResume.get();
        resume.UpdateResumeVisible(visible);

        return ResponseEntity.ok(SuccessResponse.of(200, "공개범위 수정되었습니다."));
    }

    // 이력서 상세 조회
    @Transactional(readOnly = true)
    public DtoResumeDetail getResumeDetail(String email, Long resumeId) {

        Optional<Resume> optionalResume = resumeRepository.findById(resumeId);

        Resume resume = optionalResume.get();

        DtoResumeDetail dtoResumeDetail = new DtoResumeDetail(resume);

        log.info("getGithubUrl: " + dtoResumeDetail.getGithubUrl());
        return dtoResumeDetail;
    }
}