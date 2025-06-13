package JMP.JMP.SSE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class SSEController {

    private final SSEService SSEService;

    // 구독 수락 -> 클라이언트에게 이벤트 보낼 수 있음
    @GetMapping(value = "/sse/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@RequestHeader(value = "Authorization", required = false) String token) {
        return SSEService.subscribe(token);
    }

    // 이벤트 발생 -> 구독 중인 클라이언트에게 데이터 전송
    @PostMapping("/sse/broadcast/{receiverId}")
    public void broadcast(@RequestBody EventPayload eventPayload,
                          @PathVariable Long receiverId) {
        SSEService.broadcast(receiverId, eventPayload);
        log.info(eventPayload.getMessage());
    }
}
