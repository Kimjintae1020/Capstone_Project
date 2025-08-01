package JMP.JMP.Board.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Auth.Dto.SuccessResponse;
import JMP.JMP.Board.Dto.*;
import JMP.JMP.Board.Dto.Detail.DtoBoardDetailGeneral;
import JMP.JMP.Board.Dto.Detail.DtoBoardDetailProject;
import JMP.JMP.Board.Dto.Detail.DtoBoardDetailStudy;
import JMP.JMP.Board.Dto.Response.BoardGeneralPageResponse;
import JMP.JMP.Board.Dto.Response.BoardProjectPageResponse;
import JMP.JMP.Board.Dto.Response.BoardStudyPageResponse;
import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Board.Mapper.BoardMapper;
import JMP.JMP.Board.Repository.BoardRepository;
import JMP.JMP.Comment.Repository.CommentRepository;
import JMP.JMP.Enum.BoardType;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Error.Exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static JMP.JMP.Board.Mapper.BoardMapper.toEntity;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;

    // 게시글 생성
    @Transactional
    public ResponseEntity<?> createBoard(String loginId, DtoCreateBoard dtoCreateBoard) {

        Account account = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_COMPANY));

        Board board = BoardMapper.toEntity(account, dtoCreateBoard);
        boardRepository.save(board);

        log.info("게시글 작성 성공");
        return ResponseEntity.ok(SuccessResponse.of(201, "게시글 작성이 완료되었습니다."));
    }


    // 게시글 목록 조회
    @Transactional(readOnly = true)
    public Object getBoardList(BoardType boardType, Pageable pageable, String loginId) {
        Page<Board> projects = boardRepository.findByBoardType(boardType,pageable);

        Account account = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 일반 게시글일 때
        if (boardType.equals(BoardType.GENERAL)){
            return new BoardGeneralPageResponse(
                    pageable.getPageNumber() + 1,
                    projects.getTotalPages(),
                    (int) projects.getTotalElements(),
                    projects.getContent(),
                    account.getId()
            );
        }
        // 프로젝트 게시글 일때
        else if (boardType.equals(BoardType.PROJECT_RECRUIT)){
            return new BoardProjectPageResponse(
                    pageable.getPageNumber() + 1,
                    projects.getTotalPages(),
                    (int) projects.getTotalElements(),
                    projects.getContent(),
                    account.getId()
            );
        }
        // 스터디 모집 게시글 일때
        else if (boardType.equals(BoardType.STUDY_RECRUIT)) {
            return new BoardStudyPageResponse(
                    pageable.getPageNumber() + 1,
                    projects.getTotalPages(),
                    (int) projects.getTotalElements(),
                    projects.getContent(),
                    account.getId()
            );

        }

        throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
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

    // 게시글 수정
    @Transactional
    public ResponseEntity<?> updateBoard(String loginId, DtoUpdateBoard dtoUpdateBoard, Long boardId) {

        Account findAccount = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_COMPANY));

        Board board = boardRepository.findById(boardId)
                        .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        board.updateBoard(findAccount, dtoUpdateBoard);
        boardRepository.save(board);

        return ResponseEntity.ok(SuccessResponse.of(200, "게시글이 수정되었습니다."));
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getBoardDetail(String loginId, Long boardId) {
        Account findAccount = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_COMPANY));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        board.setViewCount(board.getViewCount() + 1);

        int commentCount = commentRepository.countByBoard(board);

        if (board.getBoardType() == BoardType.GENERAL) {
            return ResponseEntity.ok(DtoBoardDetailGeneral.of(board, findAccount.getId(), commentCount));
        }

        else if (board.getBoardType() == BoardType.PROJECT_RECRUIT) {
            return ResponseEntity.ok(DtoBoardDetailProject.of(board, findAccount.getId(), commentCount));
        }

        else if (board.getBoardType() == BoardType.STUDY_RECRUIT) {
            return ResponseEntity.ok(DtoBoardDetailStudy.of(board, findAccount.getId(), commentCount));
        }

        else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    // 본인이 작성한 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getBoardMine(String loginId, BoardType boardType) {

        Account findAccount = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        List<Board> boards = boardRepository.findAllByWriter_IdAndBoardType(findAccount.getId(), boardType);

        if (boardType.equals(BoardType.GENERAL)) {
            List<DtoBoardDetailGeneral> response = boards.stream()
                    .map(board -> DtoBoardDetailGeneral.of(
                            board,
                            findAccount.getId(),
                            commentRepository.countByBoard(board)
                    ))
                    .toList();

            return ResponseEntity.ok(response);
        }

        else if (boardType.equals(BoardType.PROJECT_RECRUIT)) {
            List<DtoBoardDetailProject> response = boards.stream()
                    .map(board -> DtoBoardDetailProject.of(
                            board,
                            findAccount.getId(),
                            commentRepository.countByBoard(board)
                    ))
                    .toList();

            return ResponseEntity.ok(response);

        }
        else if (boardType.equals(BoardType.STUDY_RECRUIT)) {
            List<DtoBoardDetailStudy> response = boards.stream()
                    .map(board -> DtoBoardDetailStudy.of(
                            board,
                            findAccount.getId(),
                            commentRepository.countByBoard(board)
                    ))
                    .toList();

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(ErrorResponse.of(ErrorCode.BOARD_NOT_FOUND));
    }
}
