package JMP.JMP.Project.Service;

import JMP.JMP.Account.Dto.ErrorResponse;
import JMP.JMP.Account.Dto.SuccessResponse;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Company.Repository.CompanyRespository;
import JMP.JMP.Enum.ErrorCode;
import JMP.JMP.Enum.PostRole;
import JMP.JMP.Project.Dto.ProjectPageResponse;
import JMP.JMP.Project.Dto.DtoCreateProject;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyRespository companyRespository;

    // 프로젝트 공고 생성
    @Transactional
    public ResponseEntity<?> createProject(DtoCreateProject dtoCreateProject, String loginId) {

        Optional<Company> companyOptional = companyRespository.findByEmail(loginId);

        if (companyOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_FOUND));
        }

        Company company = companyOptional.get();

        // 관리자가 승인하지 않은 기업 담당자일 경우
        if (company.getPostRole() == PostRole.PENDING) {
            log.info("기업 담당자 권한 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NO_POST_PERMISSION));
        }

        Project project = new Project();
        project.setManager(company);
        project.setTitle(dtoCreateProject.getTitle());
        project.setDescription(dtoCreateProject.getDescription());
        project.setRequiredSkill(dtoCreateProject.getRequiredSkill());
        project.setStartDate(dtoCreateProject.getStartDate());
        project.setEndDate(dtoCreateProject.getEndDate());
        projectRepository.save(project);



        log.info("프로젝트 공고 작성 성공");
        return ResponseEntity.ok(SuccessResponse.of(201, "공고글 작성이 완료되었습니다."));
    }

    // 프로젝트 공고 목록 조회
    public ProjectPageResponse getProjectList(Pageable pageable) {

            Page<Project> posts = projectRepository.findAll(pageable);

            return new ProjectPageResponse(
                    pageable.getPageNumber() + 1,
                    posts.getTotalPages(),
                    (int) posts.getTotalElements(),
                    posts.getContent()
            );
        }
}
