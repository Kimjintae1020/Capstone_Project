package JMP.JMP.Comment.Controller;

import JMP.JMP.Comment.Service.CommentService;
import JMP.JMP.Comment.Dto.DtoCreateComment;
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
public class CommentController {

    private final CommentService commentService;
    private final JWTUtil jwtUtil;

    // 댓글 등록
    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<?> createComment(@RequestHeader(value = "Authorization", required = false) String token,
                                           @PathVariable Long boardId,
                                           @RequestBody DtoCreateComment dtoCreateComment) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        ResponseEntity<?> response = commentService.createComment(loginId, boardId, dtoCreateComment);

        return response;
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@RequestHeader(value = "Authorization", required = false) String token,
                                           @PathVariable Long commentId) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.of(ErrorCode.NOT_AUTHENTICATED));
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        ResponseEntity<?> response = commentService.deleteComment(loginId, commentId);

        return response;
    }
}
