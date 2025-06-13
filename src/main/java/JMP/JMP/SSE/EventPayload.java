package JMP.JMP.SSE;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventPayload {

    private String eventType;   // 알림타입 "apply"
    private String message;     // 알림 메시지 내용
    private String role;          // 상대방 타입 ("사용자", "기업 담당자", "관리자"
    private Long receiverId;     // 상대방 아이디
    private String senderName;  // 보낸 사람 이름
    private String timestamp;   // 이벤트 발생 시간
}
