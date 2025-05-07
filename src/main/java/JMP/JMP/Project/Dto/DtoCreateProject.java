package JMP.JMP.Project.Dto;

import JMP.JMP.Enum.RequiredSkill;
import JMP.JMP.Validation.ValidRecruitDeadline;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ValidRecruitDeadline
public class DtoCreateProject {

    private Long projectId;
    private Long managerId;
    private String title;
    private String description;
    private List<RequiredSkill> requiredSkill;
    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull(message = "모집 인원은 필수 항목입니다.")
    @Min(value = 1, message = "모집 인원은 최소 1명 이상이어야 합니다.")
    private int recruitCount;

    private LocalDate recruitDeadline;  // 프로젝트 공고 모집 마감일
}
