package JMP.JMP.Apply.Controller;

import JMP.JMP.Apply.Service.ApplyService;
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
}
