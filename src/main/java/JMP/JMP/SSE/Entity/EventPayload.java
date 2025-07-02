package JMP.JMP.SSE.Entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventPayload {

    private String eventType;   // 알림타입 "apply"
    private String message;     // 알림 메시지 내용
    @NotBlank(message = "역할은 필수 입니다.")
    private String role;          // 상대방 타입 ("사용자", "기업 담당자", "관리자")
    private Long receiverId;     // 상대방 아이디
    private String senderName;  // 보낸 사람 이름
    private String createdAt;   // 이벤트 발생 시간
}
