package JMP.JMP.AI.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Repository.ProjectRepository;
import JMP.JMP.Resume.Entity.Resume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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

    @Async
    public List<Project> recommendTopPostings(Resume resume, Account account, LocalDate startDate, LocalDate endDate, int topN) {
        List<Project> allPostings = projectRepository.findAll();

        Map<Project, Integer> scoreMap = new LinkedHashMap<>();

        for (Project posting : allPostings) {
            String prompt = promptService.generatePrompt(resume, posting, startDate, endDate, account);
            String response = promptService.callGemini(prompt);

            log.info(" Gemini 응답 (공고 ID: {}, 제목: {}): {}", posting.getProjectId(), posting.getTitle(), response);

            int score = extractTotalScore(response);
            scoreMap.put(posting, score);
        }

        return scoreMap.entrySet().stream()
                .sorted(Map.Entry.<Project, Integer>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private int extractTotalScore(String response) {
        try {
            int start = response.indexOf("\"종합 점수\":") + 8;
            int end = response.indexOf("\n", start);
            String substring = response.substring(start, end != -1 ? end : response.length());
            return Integer.parseInt(substring.replaceAll("[^0-9]", "").trim());
        } catch (Exception e) {
            return 0;
        }
    }

}