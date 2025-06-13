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
public class DtoBoardDetailProject {
    private Long boardId;
    private String title;
    private String writer;
    private String description;
    private BoardType boardType; // 카테고리
    private int recruitCount;
    private List<RequiredSkill> requiredSkills;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectWarning;
    private String applyMethod;
    private int viewCount;
    private LocalDateTime createdAt;
    private boolean isMine;
    private int commentCount;

    public static DtoBoardDetailProject of(Board board, Long currentAccountId, int commentCount) {
        return new DtoBoardDetailProject(
                board.getBoardId(),
                board.getTitle(),
                board.getWriter().getName(),
                board.getDescription(),
                board.getBoardType(),
                board.getRecruitCount(),
                board.getRequiredSkills(),
                board.getProjectStartDate(),
                board.getProjectEndDate(),
                board.getProjectWarning(),
                board.getApplyMethod(),
                board.getViewCount(),
                board.getCreatedAt(),
                board.getWriter().getId().equals(currentAccountId),
                commentCount
        );
    }

}

