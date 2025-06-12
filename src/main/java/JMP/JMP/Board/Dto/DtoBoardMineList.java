package JMP.JMP.Board.Dto;

import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Enum.BoardType;
import JMP.JMP.Enum.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoBoardMineList {

    private Long boardId;
    private String writer;
    private BoardType boardType;
    private String title;
    private String description;
    private int viewCount;
    private List<Tag> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int commentCount;       // 댓글 개수

    public static DtoBoardMineList of(Board board, int commentCount) {
        return new DtoBoardMineList(
                board.getBoardId(),
                board.getWriter().getName(),
                board.getBoardType(),
                board.getTitle(),
                board.getDescription(),
                board.getViewCount(),
                board.getTags(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                commentCount
        );
    }
}

