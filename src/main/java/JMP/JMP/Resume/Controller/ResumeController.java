package JMP.JMP.Resume.Controller;

import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Resume.Dto.DtoCreateResume;
import JMP.JMP.Resume.Dto.DtoResumeList;
import JMP.JMP.Resume.Service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResumeController {

    private final ResumeService resumeService;
    private final JWTUtil jwtUtil;

    @Value("${file.dir}")
    private String uploadDir;

    //  이력서 등록
    @PostMapping(value = "/resume/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // multipart/form-data 타입만 허용
    public ResponseEntity<?> createResume(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestPart("dto") DtoCreateResume dtoCreateResume,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {

        String savedPath = null;
        if (photo != null && !photo.isEmpty()) {

            String fileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            String resolvedPath = new File(uploadDir).getAbsolutePath();
            File file = new File(resolvedPath, fileName);

            photo.transferTo(file);
            savedPath = "/uploads/" + fileName;
        }

        ResponseEntity<?> response = resumeService.createResume(token, dtoCreateResume, savedPath);

        return response;
    }

    //  이력서 목록 조회
    @GetMapping("/resume/list")
    public List<DtoResumeList> getResumeList(@RequestHeader(value = "Authorization", required = false) String token){

        String email = jwtUtil.getUsername(token.replace("Bearer ", ""));
        List<DtoResumeList> response = resumeService.getResumeList(email);

        return response;
    }

    //  이력서 삭제
    @DeleteMapping("/resume/{resumeId}/delete")
    public ResponseEntity<?> deleteResume(@RequestHeader(value = "Authorization", required = false) String token,
                                            @PathVariable Long resumeId){

        String email = jwtUtil.getUsername(token.replace("Bearer ", ""));
        ResponseEntity<?> response = resumeService.deleteResume(email,resumeId);

        return response;
    }
}
