package JMP.JMP.AI.Controller;

import JMP.JMP.AI.Service.ProjectRecommendationService;
import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Repository.ResumeRepository;
import JMP.JMP.Account.Entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
@Slf4j
public class GeminiController {

    private final ResumeRepository resumeRepository;
    private final ProjectRecommendationService projectRecommendationService;

    @PostMapping("/completion/top3/{resumeId}")
    public ResponseEntity<Map<String, Object>> getTop3Postings(
            @PathVariable Long resumeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("이력서 없음"));

        Account account = resume.getAccount();

        Map<String, Object> result = projectRecommendationService.recommendTopPostings(resume, account, startDate, endDate, 3);

        return ResponseEntity.ok(result);
    }



}

