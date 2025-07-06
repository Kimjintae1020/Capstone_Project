package JMP.JMP.Comment.Entity;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Comment.Dto.DtoCreateComment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;   // 댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;     // 댓글 작성한 게시글

    @Column(name = "CONTENT")
    private String content;   // 댓글 내용

    @Column(name = "CREATED_AT")
    private LocalDateTime createAt;  // 댓글 생성일

    @Column(name = "UPDATED_AT")
    private LocalDateTime updateAt;

}
