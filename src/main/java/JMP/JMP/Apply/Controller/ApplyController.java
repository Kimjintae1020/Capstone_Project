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
    // To Do: 이력서 추가하여 저장
    @PostMapping("/project/{projectId}/apply")
    public ResponseEntity<?> projectApply(@RequestHeader(value = "Authorization", required = false) String token,
                                          @PathVariable Long projectId) {

        ResponseEntity<?> response = applyService.projectApply(token, projectId);

        return response;
    }
}
