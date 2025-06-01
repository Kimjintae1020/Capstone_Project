package JMP.JMP.Board.Dto;

import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Enum.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DtoBoardPage {

    private Long boardId;
    private String writer;
    private String title;           // 제목
    private String description;     // 내용
    private BoardType boardType; // 카테고리
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public DtoBoardPage(Board board) {
        this.boardId = board.getBoardId();
        this.writer = board.getWriter().getName();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.boardType = board.getBoardCategory();
    }
}
