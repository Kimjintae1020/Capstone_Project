package JMP.JMP.Project.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Apply.Entity.Apply;
import JMP.JMP.Apply.Repository.ApplyRepository;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Company.Repository.CompanyRepository;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Enum.PostRole;
import JMP.JMP.Error.Exception.CustomException;
import JMP.JMP.Error.Exception.UnauthorizedException;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Project.Dto.*;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Entity.ProjectBookmark;
import JMP.JMP.Project.Repository.ProjectBookmardRepository;
import JMP.JMP.Project.Repository.ProjectRepository;
import JMP.JMP.Resume.Dto.DtoResumeProject;
import JMP.JMP.Resume.Entity.Resume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectBookmardRepository projectBookmardRepository;
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;
    private final ApplyRepository applyRepository;
    private final JWTUtil jwtUtil;

    // 프로젝트 공고 생성
    @Transactional
    public ResponseEntity<?> createProject(DtoCreateProject dtoCreateProject, String loginId) {

        Company company = companyRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));

        // 관리자가 승인하지 않은 기업 담당자일 경우
        if (company.getPostRole() == PostRole.PENDING) {
            log.info("기업 담당자 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NO_POST_PERMISSION));
        }

        Project project = Project.createProject(company, dtoCreateProject);
        projectRepository.save(project);

        log.info("프로젝트 공고 작성 성공");
        return ResponseEntity.ok(SuccessResponse.of(201, "공고글 작성이 완료되었습니다."));
    }

    // 프로젝트 공고 목록 조회
    public ProjectPageResponse getProjectList(Pageable pageable) {

            Page<Project> projects = projectRepository.findAll(pageable);

            return new ProjectPageResponse(
                    pageable.getPageNumber() + 1,
                    projects.getTotalPages(),
                    (int) projects.getTotalElements(),
                    projects.getContent()
            );
        }

    // 프로젝트 공고 상세 조회
    @Transactional
    public DtoProjectDetail getProjectDetail(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        project.setViewCount(project.getViewCount() + 1);

        return DtoProjectDetail.of(project);
    }

    // 프로젝트 지원자 목록 조회
    public List<DtoProjectApplicants> getProjectApplicants(Long projectId, String token) throws UnauthorizedException {

        List<Apply> applyList = applyRepository.findRecentByProjectId(projectId);

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        log.info(loginId);
        boolean checkCompany = companyRepository.existsByEmail(loginId);

        if(!checkCompany){
            throw new UnauthorizedException("기업 회원만 접근할 수 있는 페이지입니다.");
        }

        return applyList.stream()
                .map(apply -> {
                    Resume resume = apply.getResume();  // 이력서를 꺼내옴

                    DtoResume resumeDto = DtoResume.builder()
                            .resumeId(resume.getResumeId())
                            .title(resume.getTitle())
                            .intro(resume.getIntro())
                            .devposition(resume.getDevposition())
                            .skills(resume.getSkills())
                            .photo(resume.getPhoto())
                            .visible(resume.isVisible())
                            .projects(resume.getProjects().stream()
                                    .map(DtoResumeProject::new)
                                    .collect(Collectors.toList()))
                            .githubUrl(resume.getGithuburl())
                            .introduce(resume.getIntroduce())
                            .build();

                    return new DtoProjectApplicants(
                            apply.getId(),
                            apply.getAccount().getId(),
                            apply.getAccount().getName(),
                            resume.getPhoto(),
                            resume.getDevposition(),
                            apply.getStatus(),
                            apply.getAppliedAt(),
                            resumeDto
                    );
                })
                .toList();

    }

    // 최근 등록한 프로젝트 공고 목록 조회
    public List<DtoProjectRecent> getProjectRecent() {
        Pageable pageale = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"));

        return projectRepository.findAll(pageale)
                .stream()
                .map(DtoProjectRecent::new)
                .collect(Collectors.toList());
    }

    // 프로젝트 공고 스크랩
    @Transactional
    public ResponseEntity<?> getProjectScrap(String token, Long projectId) {

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        Account account = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        ProjectBookmark bookmark = ProjectBookmark.addProjectBookmark(account,project);
        projectBookmardRepository.save(bookmark);

        return ResponseEntity.ok(SuccessResponse.of(200, "프로젝트 공고를 스크랩하였습니다."));
    }
}
