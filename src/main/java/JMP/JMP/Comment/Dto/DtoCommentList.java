package JMP.JMP.Comment.Dto;

import JMP.JMP.Comment.Entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DtoCommentList {

    private Long commentId;
    private String writer;
    private String content;
    private LocalDateTime createAt;
    private boolean isMine;

    public static DtoCommentList createEntity(Comment comment, Long currentAccountId) {
        return new DtoCommentList(
                comment.getCommentId(),
                comment.getAccount().getName(),
                comment.getContent(),
                comment.getCreateAt(),
                comment.getAccount().getId().equals(currentAccountId)
        );
    }
}
