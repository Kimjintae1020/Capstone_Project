package JMP.JMP.Project.Controller;

import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.Exception.UnauthorizedException;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Project.Dto.*;
import JMP.JMP.Project.Service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ProjectPageResponse> getProjectList(@RequestHeader(value = "Authorization", required = false) String token,
                                                        @RequestParam(defaultValue = "1") int page,      // 기본값 0, Service 단에서 +1 설정
                                                        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page -1, size);
        ProjectPageResponse response = projectService.getProjectList(token,pageable);

        return ResponseEntity.ok(response);

    }

    // 최근 등록한 프로젝트 공고 목록 조회
    @GetMapping("/projects/recent")
    public ResponseEntity<?> getProjectRecent() {

        List<DtoProjectRecent> projects = projectService.getProjectRecent();

        Map<String, Object> result = new HashMap<>();
        result.put("recentProjects", projects);

        return ResponseEntity.ok(result);

    }

    // 프로젝트 공고 상세조회
    @GetMapping("/project/{projectId}/detail")
    public DtoProjectDetail getProjectDetail(@PathVariable Long projectId){

        DtoProjectDetail response = projectService.getProjectDetail(projectId);

        return response;
    }

    // 프로젝트 지원자 목록 조회
    @GetMapping("/project/{projectId}/applicants")
    public ResponseEntity<List<DtoProjectApplicants>> getProjectApplicants(@RequestHeader(value = "Authorization", required = false) String token,
                                                                           @PathVariable Long projectId) throws UnauthorizedException {

        List<DtoProjectApplicants> applicants = projectService.getProjectApplicants(projectId, token);
        return ResponseEntity.ok(applicants);
    }

    // 프로젝트 공고 스크랩
    @PostMapping("/project/{projectId}/scrap")
    public ResponseEntity<?> getProjectScrap(@RequestHeader(value = "Authorization", required = false) String token,
                                             @PathVariable Long projectId) {

        ResponseEntity<?> response = projectService.getProjectScrap(token, projectId);

        return response;
    }

    // 프로젝트 공고 스크랩 목록 조회
    @GetMapping("/project/scrap/list")
    public ResponseEntity<List<DtoProjectScrap>> getProjectScrapList(@RequestHeader(value = "Authorization", required = false) String token) {

        ResponseEntity<List<DtoProjectScrap>> response = projectService.getProjectScrapList(token);

        return response;
    }

    // 프로젝트 공고 스크랩 삭제
    @DeleteMapping("/project/{projectId}/scrap/delete")
    public ResponseEntity<?> getProjectScrapDelete(@RequestHeader(value = "Authorization", required = false) String token,
                                                                       @PathVariable Long projectId) {

        ResponseEntity<?> response = projectService.getProjectScrapDelete(token,projectId);

        return response;
    }

}
