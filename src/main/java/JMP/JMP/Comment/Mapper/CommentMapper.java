package JMP.JMP.Comment.Mapper;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Comment.Dto.DtoCreateComment;
import JMP.JMP.Comment.Entity.Comment;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toEntity(Account account, Board board, DtoCreateComment dtoCreateComment) {
        Comment.CommentBuilder builder = Comment.builder()
                .account(account)
                .board(board)
                .content(dtoCreateComment.getContent())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now());
        return builder.build();
    }
}
