package JMP.JMP.Comment.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Board.Repository.BoardRepository;
import JMP.JMP.Comment.Repository.CommentRepository;
import JMP.JMP.Comment.Dto.DtoCreateComment;
import JMP.JMP.Comment.Entity.Comment;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.Exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;

    // 댓글 등록
    @Transactional
    public ResponseEntity<?> createComment(String loginId, Long boardId, DtoCreateComment dtoCreateComment) {

        Account account = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        Comment comment = Comment.createComment(account, board, dtoCreateComment);
        commentRepository.save(comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(201, "댓글작성 완료"));
    }
}
