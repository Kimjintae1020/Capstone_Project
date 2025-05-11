package JMP.JMP.Project.Controller;

import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Project.Dto.DtoCreateProject;
import JMP.JMP.Project.Dto.DtoProjectDetail;
import JMP.JMP.Project.Dto.ProjectPageResponse;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;
    private final JWTUtil jwtUtil;

    //  프로젝트 공고 작성
    @PostMapping("/create/project")
    public ResponseEntity<?> createProject(@RequestHeader(value = "Authorization", required = false) String token,
                                            @RequestBody @Valid DtoCreateProject dtoCreateProject){

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);      // ex) loginId: company@gmail.com


        ResponseEntity<?> response = projectService.createProject(dtoCreateProject,loginId);

        return response;
    }

    // 프로젝트 공고 목록 조회
    @GetMapping("/project/list")
    public ResponseEntity<ProjectPageResponse> getProjectList(@RequestParam(defaultValue = "1") int page,      // 기본값 0, Service 단에서 +1 설정
                                                        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page -1, size);
        ProjectPageResponse response = projectService.getProjectList(pageable);

        return ResponseEntity.ok(response);

    }

    // 프로젝트 공고 상세조회
    @GetMapping("/project/{projectId}/detail")
    public DtoProjectDetail getProjectDetail(@PathVariable Long projectId){

        DtoProjectDetail response = projectService.getProjectDetail(projectId);

        return response;

    }
}
