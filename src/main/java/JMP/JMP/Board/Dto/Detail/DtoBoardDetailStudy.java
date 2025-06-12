package JMP.JMP.Board.Dto.Detail;

import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Enum.BoardType;
import JMP.JMP.Enum.RequiredSkill;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoBoardDetailStudy {
    private Long boardId;
    private String title;
    private String description;
    private BoardType boardType; // 카테고리
    private int recruitCount;
    private List<RequiredSkill> requiredSkills;
    private LocalDate studyStartDate;
    private LocalDate studyEndDate;
    private String studyCurriculum;
    private String studyWarning;
    private String applyMethod;
    private int viewCount;
    private LocalDateTime createdAt;
    private boolean isMine;

    public static DtoBoardDetailStudy of(Board board, Long currentAccountId) {
        return new DtoBoardDetailStudy(
                board.getBoardId(),
                board.getTitle(),
                board.getDescription(),
                board.getBoardType(),
                board.getRecruitCount(),
                board.getRequiredSkills(),
                board.getStudyStartDate(),
                board.getStudyEndDate(),
                board.getStudyCurriculum(),
                board.getStudyWarning(),
                board.getApplyMethod(),
                board.getViewCount(),
                board.getCreatedAt(),
                board.getWriter().getId().equals(currentAccountId)
        );
    }
}

