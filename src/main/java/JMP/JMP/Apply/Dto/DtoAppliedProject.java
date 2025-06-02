package JMP.JMP.Apply.Dto;

import JMP.JMP.Enum.RequiredSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoAppliedProject {
    private Long projectId;
    private String title;
    private List<RequiredSkill> requiredSkills;
    private LocalDate recruitDeadline;
    private int viewCount;
    private LocalDate appliedAt;
}