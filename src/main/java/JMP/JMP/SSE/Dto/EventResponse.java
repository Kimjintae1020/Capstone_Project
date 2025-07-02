package JMP.JMP.SSE.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long eventId;
    private String eventType;
    private String message;
    private String senderName;
    private String createdAt;
}
