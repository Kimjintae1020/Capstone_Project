package JMP.JMP.Board.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Board.Dto.BoardPageResponse;
import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Board.Repository.BoardRepository;
import JMP.JMP.Board.Dto.DtoCreateBoard;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.Exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;

    // 게시글 생성
    @Transactional
    public ResponseEntity<?> createBoard(String loginId, DtoCreateBoard dtoCreateBoard) {

        Account account = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_COMPANY));

        Board board = Board.createBoard(account, dtoCreateBoard);
        boardRepository.save(board);

        log.info("게시글 작성 성공");
        return ResponseEntity.ok(SuccessResponse.of(201, "게시글 작성이 완료되었습니다."));
    }


    // 게시글 목록 조회
    public BoardPageResponse getBoardList(Pageable pageable) {
        Page<Board> projects = boardRepository.findAll(pageable);

        return new BoardPageResponse(
                pageable.getPageNumber() + 1,
                projects.getTotalPages(),
                (int) projects.getTotalElements(),
                projects.getContent()
        );
    }

    // 게시글 삭제
    @Transactional
    public ResponseEntity<?> deleteBoard(String loginId, Long boardId) {

        Account findAccount = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_COMPANY));

        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if (!findAccount.getId().equals(findBoard.getWriter().getId())) {
            throw new CustomException(ErrorCode.INVALID_ACCESS);
        }

        boardRepository.delete(findBoard);

        log.info("게시글 삭제 성공");
        return ResponseEntity.ok(SuccessResponse.of(200, "게시글이 삭제되었습니다."));
    }
}
