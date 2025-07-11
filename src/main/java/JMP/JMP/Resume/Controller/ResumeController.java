package JMP.JMP.Resume.Controller;

import JMP.JMP.Auth.Security.JWTUtil;
import JMP.JMP.Resume.Dto.DtoCreateResume;
import JMP.JMP.Resume.Dto.DtoResumeDetail;
import JMP.JMP.Resume.Dto.DtoResumeList;
import JMP.JMP.Resume.Dto.DtoUpdateResume;
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

    //  이력서 등록
    @PostMapping(value = "/resume/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // multipart/form-data 타입만 허용
    public ResponseEntity<?> createResume(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestPart("dto") DtoCreateResume dtoCreateResume,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {


        ResponseEntity<?> response = resumeService.createResume(token, dtoCreateResume, photo);

        return response;
    }

    //  이력서 목록 조회
    @GetMapping("/resume/list")
    public List<DtoResumeList> getResumeList(@RequestHeader(value = "Authorization", required = false) String token){

        String email = jwtUtil.getUsername(token.replace("Bearer ", ""));
        List<DtoResumeList> response = resumeService.getResumeList(email);

        return response;
    }

    //  이력서 상세 조회
    @GetMapping("/resume/{resumeId}/detail")
    public DtoResumeDetail getResumeDetail(@RequestHeader(value = "Authorization", required = false) String token,
                                           @PathVariable Long resumeId){

        String email = jwtUtil.getUsername(token.replace("Bearer ", ""));
        DtoResumeDetail response = resumeService.getResumeDetail(email,resumeId);

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


    // 이력서 수정
    @PutMapping(value = "/resume/{resumeId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateResume(@RequestHeader(value = "Authorization", required = false) String token,
                                          @PathVariable Long resumeId,
                                          @RequestPart("dto") DtoUpdateResume dtoUpdateResume,
                                          @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {

        String email = jwtUtil.getUsername(token.replace("Bearer ", ""));

        ResponseEntity<?> response = resumeService.updateResume(email,resumeId,dtoUpdateResume,photo);

        return response;
    }

    // 이력서 공개범위 수정
    @PatchMapping("/resume/{resumeId}/visible")
    public ResponseEntity<?> visibleResume(@RequestHeader(value = "Authorization", required = false) String token,
                                           @PathVariable Long resumeId,
                                           @RequestParam boolean visible){

        String email = jwtUtil.getUsername(token.replace("Bearer ", ""));
        ResponseEntity<?> response = resumeService.updateResumevisible(email,resumeId,visible);

        return response;
    }




}
