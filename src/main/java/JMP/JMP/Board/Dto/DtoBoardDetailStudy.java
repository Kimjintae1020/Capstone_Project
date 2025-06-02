package JMP.JMP.Board.Dto;

import JMP.JMP.Enum.RequiredSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoBoardDetailStudy {
    private Long boardId;
    private String title;
    private String description;
    private int recruitCount;
    private List<RequiredSkill> requiredSkills;
    private LocalDate studyStartDate;
    private LocalDate studyEndDate;
    private String studyCurriculum;
    private String studyWarning;
    private String applyMethod;
    private int viewCount;
    private LocalDate createdAt;
}

