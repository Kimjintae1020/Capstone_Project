package JMP.JMP.Project.Controller;

import JMP.JMP.Account.Dto.ErrorResponse;
import JMP.JMP.Enum.ErrorCode;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Project.Dto.DtoCreateProject;
import JMP.JMP.Project.Service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                                            @RequestBody DtoCreateProject dtoCreateProject){

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        ResponseEntity<?> response = projectService.createProject(dtoCreateProject,loginId);

        return response;
    }
}
