package JMP.JMP.Board.Controller;

import JMP.JMP.Board.Service.BoardService;
import JMP.JMP.Board.Dto.DtoCreateBoard;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardServie;
    private final JWTUtil jwtUtil;

    @PostMapping("/create/board")
    public ResponseEntity<?> createBoard(@RequestHeader(value = "Authorization", required = false) String token,
                                           @RequestBody DtoCreateBoard dtoCreateBoard){

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);      // ex) loginId: company@gmail.com


        ResponseEntity<?> response = boardServie.createBoard(loginId,dtoCreateBoard);

        return response;
    }
}
