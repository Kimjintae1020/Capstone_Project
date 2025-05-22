package JMP.JMP.Resume.Dto;

import JMP.JMP.Enum.RequiredSkill;
import JMP.JMP.Resume.Entity.Resume;
import JMP.JMP.Resume.Entity.ResumeProject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoResumeProject {

    @NotBlank
    private String name;

    @Size(max = 500)
    private String description;

    private List<RequiredSkill> techStack;
    private String githubLink;   // 프로젝트 깃허브 링크
    private LocalDate startDate; // 프로젝트 시작일
    private LocalDate endDate;   // 프로젝트 마감일

    public DtoResumeProject(ResumeProject resume) {
        this.name = resume.getName();
        this.description = resume.getDescription();
        this.techStack = resume.getTechStack();
        this.githubLink = resume.getGithubLink();
        this.startDate = resume.getStartDate();
        this.endDate = resume.getEndDate();
    }

}

