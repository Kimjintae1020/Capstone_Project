package JMP.JMP.Resume.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.Exception.CustomException;
import JMP.JMP.Auth.Security.JWTUtil;
import JMP.JMP.Resume.Dto.*;
import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Entity.ResumeProject;
import JMP.JMP.Resume.Mapper.ResumeMapper;
import JMP.JMP.Resume.Repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
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

        Account account = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Resume savedResume = ResumeMapper.toEntity(account, dtoCreateResume, savedPath);
        resumeRepository.save(savedResume);

        return ResponseEntity.ok(CreateResumeSuccessResponse.of(201, "이력서 작성이 완료되었습니다.", savedResume.getResumeId()));
    }

    //  이력서 목록 조회
    @Transactional(readOnly = true)
    public List<DtoResumeList> getResumeList(String email) {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

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

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Resume resume = resumeRepository.findByResumeIdAndAccountId(resumeId, account.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));

        resumeRepository.delete(resume);

        return ResponseEntity.ok().body("이력서가 성공적으로 삭제되었습니다.");
    }

    // 이력서 수정 기능
    @Transactional
    public ResponseEntity<?> updateResume(String email, Long resumeId, DtoUpdateResume dtoUpdateResume, String photo) {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Resume resume = resumeRepository.findByResumeIdAndAccountId(resumeId, account.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));

        resume.setAccount(account);
        resume.setTitle(dtoUpdateResume.getTitle());
        resume.setIntro(dtoUpdateResume.getIntro());
        resume.setSkills(dtoUpdateResume.getSkills());
        resume.setGithuburl(dtoUpdateResume.getGithubUrl());
        resume.setVisible(dtoUpdateResume.isVisible());
        resume.setDevposition(dtoUpdateResume.getDevposition());
        resume.setPhoto(photo);
        resume.setIntroduce(dtoUpdateResume.getIntroduce());

        resume.getProjects().clear();

        List<ResumeProject> resumeProjects = dtoUpdateResume.getProjects().stream()
                .map(dtoProject -> ResumeProject.builder()
                        .name(dtoProject.getName())
                        .description(dtoProject.getDescription())
                        .techStack(dtoProject.getTechStack())
                        .githubLink(dtoProject.getGithubLink())
                        .startDate(dtoProject.getStartDate())
                        .endDate(dtoProject.getEndDate())
                        .resume(resume)
                        .build())
                .collect(Collectors.toList());

        resume.getProjects().addAll(resumeProjects);

        resume.setUpdatedAt(LocalDate.now());

        resumeRepository.save(resume);
        return ResponseEntity.ok(SuccessResponse.of(200, "이력서가 성공적으로 수정되었습니다."));
    }

    // 이력서 공개범위 수정
    @Transactional
    public ResponseEntity<?> updateResumevisible(String email, Long resumeId, boolean visible) {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Resume resume = resumeRepository.findByResumeIdAndAccountId(resumeId, account.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_OWNED));

        resume.UpdateResumeVisible(visible);

        return ResponseEntity.ok(SuccessResponse.of(200, "공개범위 수정되었습니다."));
    }

    // 이력서 상세 조회
    @Transactional(readOnly = true)
    public DtoResumeDetail getResumeDetail(String email, Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_OWNED));

        DtoResumeDetail dtoResumeDetail = new DtoResumeDetail(resume);

        log.info("getGithubUrl: " + dtoResumeDetail.getGithubUrl());
        return dtoResumeDetail;
    }
}