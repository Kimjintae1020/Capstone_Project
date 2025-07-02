package JMP.JMP.SSE.Mapper;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Enum.Role;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.SSE.Entity.Event;
import JMP.JMP.SSE.Entity.EventPayload;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEntity(EventPayload eventPayload){
        return Event.builder()
                .eventType(eventPayload.getEventType())
                .message(eventPayload.getMessage())
                .role(Role.valueOf(eventPayload.getRole()))
                .receiverId(eventPayload.getReceiverId())
                .senderName(eventPayload.getSenderName())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static EventPayload toApplyPayload(Project project, Account sender){
        return new EventPayload(
                "apply",
                sender.getName() + " 님이 '" + project.getTitle() + "' 프로젝트에 지원하셨습니다.",
                project.getManager().getRole().name(),
                project.getManager().getId(),
                sender.getName(),
                LocalDate.now().toString()
        );
    }
}
