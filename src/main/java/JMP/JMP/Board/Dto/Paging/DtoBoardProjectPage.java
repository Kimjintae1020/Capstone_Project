package JMP.JMP.Board.Dto.Paging;

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
public class DtoBoardProjectPage {

    private Long boardId;
    private String writer;
    private String title;           // 제목
    private String description;     // 내용
    private BoardType boardType; // 카테고리
    private int viewCount;
    private List<RequiredSkill> skills;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectWarning;   // 프로젝트 주의사항
    private String applyMethod;    // 지원 방법
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isMine;         // 본인이 작성한 글인지 여부를 알려주는 컬럼


    public DtoBoardProjectPage(Board board, Long currentAccountId) {
        this.boardId = board.getBoardId();
        this.writer = board.getWriter().getName();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.skills = board.getRequiredSkills();
        this.projectStartDate = board.getProjectStartDate();
        this.projectEndDate = board.getProjectEndDate();
        this.projectWarning = board.getProjectWarning();
        this.applyMethod = board.getApplyMethod();
        this.boardType = board.getBoardType();
        this.viewCount = board.getViewCount();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.isMine = board.getWriter().getId().equals(currentAccountId);
    }
}
