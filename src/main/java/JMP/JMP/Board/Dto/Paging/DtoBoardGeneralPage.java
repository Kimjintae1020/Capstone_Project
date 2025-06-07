package JMP.JMP.Board.Dto.Paging;

import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Enum.BoardType;
import JMP.JMP.Enum.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DtoBoardGeneralPage {

    private Long boardId;
    private String writer;
    private String title;           // 제목
    private String description;     // 내용
    private BoardType boardType; // 카테고리
    private int viewCount;
    private int likeCount;                                  // 좋아요
    private List<Tag> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isMine;         // 본인이 작성한 글인지 여부를 알려주는 컬럼
    private int commentCount;       // 게시글 댓글 개수

    public DtoBoardGeneralPage(Board board, Long currentAccountId) {
        this.boardId = board.getBoardId();
        this.writer = board.getWriter().getName();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.tags = board.getTags();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.boardType = board.getBoardType();
        this.viewCount = board.getViewCount();
        this.likeCount = board.getLikeCount();
        this.isMine = board.getWriter().getId().equals(currentAccountId);
        this.commentCount = board.getComments().size();
    }
}
