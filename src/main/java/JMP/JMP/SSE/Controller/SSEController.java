package JMP.JMP.SSE.Controller;

import JMP.JMP.Enum.Role;
import JMP.JMP.SSE.Entity.EventPayload;
import JMP.JMP.SSE.Dto.EventResponse;
import JMP.JMP.SSE.Service.SSEService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

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
    @PostMapping("/sse/broadcast")
    public void broadcast(@RequestBody EventPayload eventPayload) {

        SSEService.broadcast(eventPayload);
        log.info(eventPayload.getMessage());
    }

    // 받은 알림 목록 조회
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedEvents(@RequestHeader(value = "Authorization", required = false) String token,
                                               @RequestParam Role role) {
        List<EventResponse> events = SSEService.getReceivedEvents(token,role);
        return ResponseEntity.ok(events);
    }
}
