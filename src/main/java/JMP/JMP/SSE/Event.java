package JMP.JMP.SSE;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "EVENT")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private Long eventId;

    @Column(name = "EVENT_TYPE")
    private String eventType;   // 알림 타입

    @Column(name = "MESSAGE")
    private String message;   // 알림 메시지 내용

    @Column(name = "ROLE")
    private Role role;   // 알림 메시지 내용

    @Column(name = "RECEIVER_ID")
    private Long receiverId;   // 상대방 아이디

    @Column(name = "SENDERNAME")
    private String senderName;   // 보낸사람 이름

    @Column(name = "TIMESTAMP")
    private LocalDateTime createdAt;   // 알림 메시지 내용

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
