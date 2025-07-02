package JMP.JMP.Comment.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Board.Repository.BoardRepository;
import JMP.JMP.Comment.Dto.DtoCommentList;
import JMP.JMP.Comment.Mapper.CommentMapper;
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

import java.util.List;

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

        Comment comment = CommentMapper.toEntity(account, board, dtoCreateComment);
        commentRepository.save(comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(201, "댓글작성 완료"));
    }

    // 댓글 삭제
    @Transactional
    public ResponseEntity<?> deleteComment(String loginId, Long commentId) {
        Account account = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));


        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(()-> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 본인이 작성한 댓글 검증
        if (!account.getId().equals(comment.getAccount().getId())) {
            throw new CustomException(ErrorCode.INVALID_COMMENT_ACCESS);
        }

        commentRepository.deleteById(commentId);

        log.info("댓글 삭제 성공");
        return ResponseEntity.ok(SuccessResponse.of(200, "댓글이 삭제되었습니다."));
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getCommentList(String loginId, Long boardId) {
        Account account = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        List<DtoCommentList> commentList = commentRepository.findAllByBoard_BoardId(boardId)
                .stream()
                .map(comment -> DtoCommentList.createEntity(comment, account.getId()))
                .toList();

        return ResponseEntity.ok(commentList);
    }
}
