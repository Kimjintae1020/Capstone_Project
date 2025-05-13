package JMP.JMP.AI.Controller;

import JMP.JMP.AI.Service.ProjectRecommendationService;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Repository.ResumeRepository;
import JMP.JMP.Account.Entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
@Slf4j
public class GeminiController {

    private final ResumeRepository resumeRepository;
    private final ProjectRecommendationService projectRecommendationService;

    @PostMapping("/completion/top3/{resumeId}")
    public ResponseEntity<List<Map<String, Object>>> getTop3Postings(@PathVariable Long resumeId,
                                                                     @RequestParam String duration) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("이력서 없음"));

        Account account = resume.getAccount();

        List<Project> topPostings = projectRecommendationService.recommendTopPostings(resume, account, duration, 3);

        List<Map<String, Object>> result = topPostings.stream().map(posting -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", posting.getProjectId());
            map.put("title", posting.getTitle());
            map.put("requiredSkill", posting.getRequiredSkill());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
