package JMP.JMP.AI.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Resume.Entity.Resume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratePromptService {

    private final GeminiService geminiService;

    public String callGemini(String prompt) {
        return geminiService.getCompletion(prompt);
    }

    public String generatePrompt(Resume resume, Project project, LocalDate startDate, LocalDate endDate, Account account) {

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

        log.info("projectTechStack {}", projectTechStack);

        return """
            아래는 한 구직자의 이력서 정보와 프로젝트 공고 정보입니다.
            이 정보를 참고하여 구직자의 해당 프로젝트 적합도를 평가해주세요.

            평가 기준:
            1. 기술 스택 일치도 (0~100점)
            2. 학력 선호도 일치도 (0~100점)
            3. 기간 일치도 (0~100점)
            4. 직무 일치도 (0~100점)
            5. 프로젝트 경험 기술의 유사성 (0~100점)
            6. 종합 점수 (0~100점) - 위 항목들을 종합한 최종 점수

            출력 형식 (반드시 지켜주세요):
            1. JSON 코드 블록 없이 `{}` 형태로 작성 (예: ```json 금지)
            2. JSON 바로 아래, 한두 문장의 간단한 요약 작성 (100자 이내)

            출력 예시:
            { "기술 스택": 85, "학력": 90, "기간": 100, "직무": 80, "프로젝트 경험": 88, "종합 점수": 89 }
            요약: 프론트엔드 경험은 풍부하지만 백엔드 경험이 부족하여 점수가 일부 낮아짐.

            구직자 이력서:
            기술 스택: %s
            학력: %s
            희망 직무: %s
            보유 프로젝트 기술 스택: %s

            프로젝트 공고:
            제목: %s
            요구 기술 스택: %s
            기간: %s ~ %s
            설명: %s
            """
                .formatted(
                        resume.getSkills(),
                        account.getEducation(),
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
