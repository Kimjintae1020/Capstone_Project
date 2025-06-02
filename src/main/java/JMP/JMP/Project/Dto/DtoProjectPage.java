package JMP.JMP.Project.Dto;

import JMP.JMP.Enum.RequiredSkill;
import JMP.JMP.Project.Entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoProjectPage {

    private Long projectId;
    private String managername;        // 작성자 이름
    private String title;
    private String description;
    private int viewCount;          // 조회수
    private List<RequiredSkill> requiredSkill;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate recruitDeadline;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private boolean isScrapped;

    public DtoProjectPage(Project project, boolean isScrapped) {
        this.projectId = project.getProjectId();
        this.managername = project.getManager().getName();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.viewCount = project.getViewCount();
        this.requiredSkill = project.getRequiredSkill();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.recruitDeadline = project.getRecruitDeadline();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
        this.isScrapped = isScrapped;
    }

}
