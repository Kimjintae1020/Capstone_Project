package JMP.JMP.Apply.Controller;

import JMP.JMP.Apply.Service.ApplyService;
import JMP.JMP.Enum.ApplyStatus;
import JMP.JMP.Error.Exception.UnauthorizedException;
import JMP.JMP.Project.Service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApplyController {

    private final ApplyService applyService;

    //  프로젝트 공고 지원
    @PostMapping("/project/{projectId}/apply/{resumeId}")
    public ResponseEntity<?> projectApply(@RequestHeader(value = "Authorization", required = false) String token,
                                          @PathVariable Long projectId,
                                          @PathVariable Long resumeId) {

        ResponseEntity<?> response = applyService.projectApply(token, projectId, resumeId);

        return response;
    }

    // 프로젝트 지원자 선정
    @PatchMapping("/projects/{projectId}/applicants/{applyId}/status")
    public ResponseEntity<?> updateApplyStatus(@RequestHeader(value = "Authorization", required = false) String token,
                                               @PathVariable Long projectId,
                                               @PathVariable Long applyId,
                                               @RequestParam("status") ApplyStatus status ){

        ResponseEntity<?> response = applyService.updateApplyStatus(token, projectId,applyId,status);

        return response;
    }
}
