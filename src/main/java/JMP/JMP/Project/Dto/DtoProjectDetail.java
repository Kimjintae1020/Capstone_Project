package JMP.JMP.Project.Dto;

import JMP.JMP.Enum.RequiredSkill;
import JMP.JMP.Project.Entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoProjectDetail {

    private String managername;        // 작성자 이름
    private String title;
    private String description;
    private int viewCount;          // 조회수
    private List<RequiredSkill> requiredSkill;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate recruitDeadline;
    private int recruitCount;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public static DtoProjectDetail of(Project project) {
        return new DtoProjectDetail(
                project.getManager().getName(),
                project.getTitle(),
                project.getDescription(),
                project.getViewCount(),
                project.getRequiredSkill(),
                project.getStartDate(),
                project.getEndDate(),
                project.getRecruitDeadline(),
                project.getRecruitCount(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
