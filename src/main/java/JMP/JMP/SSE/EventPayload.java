package JMP.JMP.SSE;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventPayload {

    private String eventType;   // 알림타입 "apply"
    private String message;     // 알림 메시지 내용
    private Long relatedId;     // 관련 게시글 ID, 댓글 ID 등
    private String senderName;  // 보낸 사람 이름
    private String timestamp;   // 이벤트 발생 시간
}
