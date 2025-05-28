package JMP.JMP.AI.Service;

import JMP.JMP.AI.Dto.ProjectEvaluationResult;
import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Repository.ProjectRepository;
import JMP.JMP.Resume.Entity.Resume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectRecommendationService {

    private final ProjectRepository projectRepository;
    private final GeneratePromptService promptService;

    public Map<String, Object> recommendTopPostings(
            Resume resume, Account account, LocalDate startDate, LocalDate endDate, int topN) {

        List<Project> allPostings = projectRepository.findAll();

        List<ProjectEvaluationResult> projectResults = new ArrayList<>();

        for (Project posting : allPostings) {
            String prompt = promptService.generatePrompt(resume, posting, startDate, endDate, account);
            String response = promptService.callGemini(prompt);

            log.info("Gemini 응답 (공고 ID: {}, 제목: {}):\n{}", posting.getProjectId(), posting.getTitle(), response);

            int score = extractTotalScore(response);
            String details = extractSummaryDetails(response);

            projectResults.add(new ProjectEvaluationResult(
                    posting.getProjectId(),
                    posting.getTitle(),
                    posting.getRequiredSkill().stream().map(Enum::name).toList(),
                    score,
                    details
            ));
        }

        List<ProjectEvaluationResult> topProjects = projectResults.stream()
                .sorted(Comparator.comparingInt(ProjectEvaluationResult::getScore).reversed())
                .limit(topN)
                .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("recommendations", topProjects);
        result.put("userSkills", resume.getSkills().stream().map(Enum::name).toList());

        return result;
    }

    // 종합점수 산정
    private int extractTotalScore(String response) {
        try {
            int start = response.indexOf("\"종합 점수\":") + 8;
            int end = response.indexOf(",", start);
            String substring = response.substring(start, end != -1 ? end : response.length());
            return Integer.parseInt(substring.replaceAll("[^0-9]", "").trim());
        } catch (Exception e) {
            return 0;
        }
    }

    // 종합점수 산정 내용 요약
    private String extractSummaryDetails(String response) {
        if (response == null) return "";

        String[] lines = response.split("요약:");
        return lines.length > 1 ? lines[1].trim() : "";
    }
}
