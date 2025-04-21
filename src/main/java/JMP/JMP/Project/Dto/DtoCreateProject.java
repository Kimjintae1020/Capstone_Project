package JMP.JMP.Project.Dto;

import JMP.JMP.Enum.RequiredSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtoCreateProject {

    private Long projectId;
    private Long managerId;
    private String title;
    private String description;
    private List<RequiredSkill> requiredSkill;
    private LocalDate startDate;
    private LocalDate endDate;
}
