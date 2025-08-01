package JMP.JMP.Board.Controller;

import JMP.JMP.Board.Dto.DtoUpdateBoard;
import JMP.JMP.Board.Service.BoardService;
import JMP.JMP.Board.Dto.DtoCreateBoard;
import JMP.JMP.Enum.BoardType;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Auth.Security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardServie;
    private final JWTUtil jwtUtil;

    // 게시글 생성
    @PostMapping("/create/board")
    public ResponseEntity<?> createBoard(@RequestHeader(value = "Authorization", required = false) String token,
                                           @RequestBody DtoCreateBoard dtoCreateBoard){

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);


        ResponseEntity<?> response = boardServie.createBoard(loginId,dtoCreateBoard);

        return response;
    }

    // 게시글 목록 조회 [페이징]
    @GetMapping("/board/list")
    public ResponseEntity<Object> getboardList(@RequestHeader(value = "Authorization", required = false) String token,
                                               @RequestParam(required = false) BoardType boardType,
                                               @RequestParam(defaultValue = "1") int page,      // 기본값 0, Service 단에서 +1 설정
                                               @RequestParam(defaultValue = "10") int size) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        Pageable pageable = PageRequest.of(page -1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Object response = boardServie.getBoardList(boardType,pageable,loginId);

        return ResponseEntity.ok(response);
    }

    // 게시글 삭제
    @DeleteMapping("/boards/{boardId}/delete")
    public ResponseEntity<?> deleteBoard(@RequestHeader(value = "Authorization", required = false) String token,
                                         @PathVariable Long boardId){

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);


        ResponseEntity<?> response = boardServie.deleteBoard(loginId,boardId);

        return response;
    }

    // 게시글 수정
    @PutMapping("/boards/{boardId}/update")
    public ResponseEntity<?> updateBoard(@RequestHeader(value = "Authorization", required = false) String token,
                                         @PathVariable Long boardId,
                                         @RequestBody DtoUpdateBoard dtoUpdateBoard){

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);


        ResponseEntity<?> response = boardServie.updateBoard(loginId,dtoUpdateBoard,boardId);

        return response;
    }

    // 게시글 상세 조회
    @GetMapping("/boards/{boardId}/detail")
    public ResponseEntity<?> getBoardDetail(@RequestHeader(value = "Authorization", required = false) String token,
                                            @PathVariable Long boardId){

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);


        ResponseEntity<?> response = boardServie.getBoardDetail(loginId,boardId);

        return response;
    }

    // 본인이 작성한 게시글 조회
    @GetMapping("/boards/mine")
    public ResponseEntity<?> getBoardMine(@RequestHeader(value = "Authorization", required = false) String token,
                                          @RequestParam(required = false) BoardType boardType) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);


        ResponseEntity<?> response = boardServie.getBoardMine(loginId, boardType);

        return response;
    }
}
