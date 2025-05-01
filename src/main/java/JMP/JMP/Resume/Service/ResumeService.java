package JMP.JMP.Resume.Service;

import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.Resume.Dto.DtoResume;
import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final AccountRepository accountRepository;
    private final JWTUtil jwtUtil;

    // 이력서 등록
    @Transactional
    // To Do: 매칭 데이터 -> 매칭 API -> AI 분석 -> 공고 추천
    public ResponseEntity<?> createResume(String token, DtoResume dtoResume) {

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        Optional<Account> accountOptional = accountRepository.findByEmail(loginId);

        if (accountOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_FOUND));
        }

        Account account = accountOptional.get();

        Resume resume = new Resume();
            resume.setAccount(account);
            resume.setTitle(dtoResume.getTitle());
            resume.setContent(dtoResume.getContent());
            resume.setSkill(dtoResume.getSkill());
            resume.setResumeFileUrl(dtoResume.getResumeFileUrl());
            resume.setVisible(dtoResume.isVisible());
            resume.setCreatedAt(dtoResume.getCreatedAt());
            resume.setUpdatedAt(dtoResume.getUpdatedAt());

        resumeRepository.save(resume);

        return ResponseEntity.ok(SuccessResponse.of(201, "이력서 작성이 완료되었습니다."));
    }
}
