package JMP.JMP.SSE;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.ErrorResponse;
import JMP.JMP.Error.Exception.CustomException;
import JMP.JMP.Jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SSEService {

    private final SSERepository SSERepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final JWTUtil jwtUtil;
    private final AccountRepository accountRepository;


    public SseEmitter subscribe(String token) {

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);      // ex) loginId: company@gmail.com

        Account findAccount = accountRepository.findByEmail(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // sse의 유효 시간이 만료되면, 클라이언트에서 다시 서버로 이벤트 구독을 시도
        SseEmitter sseEmitter = SSERepository.save(findAccount.getId(), new SseEmitter(DEFAULT_TIMEOUT));

        // 사용자에게 모든 데이터가 전송되었다면 emitter 삭제
        sseEmitter.onCompletion(() -> SSERepository.deleteById(findAccount.getId()));

        // Emitter의 유효 시간이 만료되면 emitter 삭제
        // 유효 시간이 만료되었다는 것은 클라이언트와 서버가 연결된 시간동안 아무런 이벤트가 발생하지 않은 것
        sseEmitter.onTimeout(() -> SSERepository.deleteById(findAccount.getId()));

        // 첫 구독시에 이벤트를 발생시킨다.
        // sse 연결이 이루어진 후, 하나의 데이터로 전송되지 않는다면 sse의 유효 시간이 만료되고 503 에러가 발생
        sendToClient(findAccount.getId(), "SSE 구독, 구독한 유저 ID : " + findAccount.getId());

        return sseEmitter;
    }

    // 구독되어 있는 클라이언트에게 데이터 전송
    public void broadcast(Long receiverId, EventPayload eventPayload) {
        sendToClient(receiverId, eventPayload);
    }

    // 클라이언트에게 이벤트 전송
    private void sendToClient(Long accountId, Object data) {
        SseEmitter sseEmitter = SSERepository.findById(accountId);
        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .id(accountId.toString())
                            .name("sse")
                            .data(data)
            );
        } catch (IOException ex) {
            SSERepository.deleteById(accountId);
            throw new RuntimeException("연결 오류 발생");
        }
    }
}
