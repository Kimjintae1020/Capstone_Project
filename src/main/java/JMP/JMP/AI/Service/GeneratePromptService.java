package JMP.JMP.AI.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Resume.Entity.Resume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratePromptService {

    private final GeminiService geminiService;

    public String callGemini(String prompt) {
        return geminiService.getCompletion(prompt);
    }

    public String generatePrompt(Resume resume, Project project, String desiredDuration, Account account) {


        String projectTechStack = resume.getProjects().isEmpty()
                ? "없음"
                : resume.getProjects().stream()
                .flatMap(p -> p.getTechStack().stream())
                .distinct()
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        String requiredSkills = project.getRequiredSkill().stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        log.info("projectTechStack {}",projectTechStack);
        return """
            아래는 한 구직자의 이력서와 한 프로젝트 공고입니다.
            두 정보를 비교하여 다음 다섯 가지 기준에 따라 적합도를 평가해 주세요:
            1. 기술 스택 일치도
            2. 학력 선호도 일치도
            3. 기간 일치도
            4. 직무 일치도
            5. 프로젝트 경험 기술의 유사성

            각 항목을 100점 만점으로 평가하고, 마지막에 종합 점수를 함께 제공해주세요.
            결과는 아래 JSON 형식처럼 출력해주세요:
            {
              \"기술 스택\": 90,
              \"학력\": 85,
              \"기간\": 95,
              \"직무\": 80,
              \"프로젝트 경험\": 88,
              \"종합 점수\": 88
            }

            [이력서]
            기술 스택: %s
            학력: %s
            가능 기간: %s
            희망 직무: %s
            프로젝트 경험 기술: %s

            [프로젝트 공고]
            제목: %s
            요구 기술 스택: %s
            시작일: %s
            종료일: %s
            기타 설명: %s
            """
                .formatted(
                resume.getSkills(),
                account.getEducation(),
                desiredDuration,
                resume.getDevposition(),
                projectTechStack,
                project.getTitle(),
                requiredSkills,
                project.getStartDate(),
                project.getEndDate(),
                project.getDescription()
        );
    }
}