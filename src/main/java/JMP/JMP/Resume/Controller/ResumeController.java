package JMP.JMP.Resume.Controller;

import JMP.JMP.Resume.Dto.DtoCreateResume;
import JMP.JMP.Resume.Service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResumeController {

    private final ResumeService resumeService;

    //  이력서 등록
    @PostMapping("/resume/create")
    public ResponseEntity<?> createResume(@RequestHeader(value = "Authorization", required = false) String token,
                                          @RequestBody DtoCreateResume dtoCreateResume){

        ResponseEntity<?> response = resumeService.createResume(token, dtoCreateResume);

        return response;
    }
}
