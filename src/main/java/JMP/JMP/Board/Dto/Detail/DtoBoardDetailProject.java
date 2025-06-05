package JMP.JMP.Board.Dto.Detail;

import JMP.JMP.Enum.RequiredSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoBoardDetailProject {
    private Long boardId;
    private String title;
    private String description;
    private int recruitCount;
    private List<RequiredSkill> requiredSkills;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectWarning;
    private String applyMethod;
    private int viewCount;
    private LocalDateTime createdAt;
}

