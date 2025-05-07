package JMP.JMP.Resume.Controller;

import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Resume.Dto.DtoCreateResume;
import JMP.JMP.Resume.Dto.DtoResumeList;
import JMP.JMP.Resume.Service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResumeController {

    private final ResumeService resumeService;
    private final JWTUtil jwtUtil;

    //  이력서 등록
    @PostMapping("/resume/create")
    public ResponseEntity<?> createResume(@RequestHeader(value = "Authorization", required = false) String token,
                                          @RequestBody DtoCreateResume dtoCreateResume){

        ResponseEntity<?> response = resumeService.createResume(token, dtoCreateResume);

        return response;
    }

    //  이력서 목록 조회
    @GetMapping("/resume/list")
    public List<DtoResumeList> getResumeList(@RequestHeader(value = "Authorization", required = false) String token){

        String email = jwtUtil.getUsername(token.replace("Bearer ", ""));
        List<DtoResumeList> response = resumeService.getResumeList(email);

        return response;
    }
}
