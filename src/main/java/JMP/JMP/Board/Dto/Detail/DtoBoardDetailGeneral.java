package JMP.JMP.Board.Dto.Detail;

import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Enum.BoardType;
import JMP.JMP.Enum.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoBoardDetailGeneral {
    private Long boardId;
    private String title;
    private String writer;
    private String description;
    private BoardType boardType; // 카테고리
    private List<Tag> tags;
    private int viewCount;
    private LocalDateTime createdAt;
    private boolean isMine;
    private int commentCount;


    public static DtoBoardDetailGeneral of(Board board, Long currentAccountId, int commentCount) {
        return new DtoBoardDetailGeneral(
                board.getBoardId(),
                board.getTitle(),
                board.getWriter().getName(),
                board.getDescription(),
                board.getBoardType(),
                board.getTags(),
                board.getViewCount(),
                board.getCreatedAt(),
                board.getWriter().getId().equals(currentAccountId),
                commentCount
        );

    }
}

