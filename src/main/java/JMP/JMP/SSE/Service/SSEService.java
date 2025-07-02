package JMP.JMP.SSE.Service;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Account.Repository.AccountRepository;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Company.Repository.CompanyRepository;
import JMP.JMP.Enum.Role;
import JMP.JMP.Error.ErrorCode;
import JMP.JMP.Error.Exception.CustomException;
import JMP.JMP.Jwt.JWTUtil;
import JMP.JMP.SSE.Dto.EventResponse;
import JMP.JMP.SSE.Entity.Event;
import JMP.JMP.SSE.Entity.EventPayload;
import JMP.JMP.SSE.Mapper.EventMapper;
import JMP.JMP.SSE.Repository.EventRepository;
import JMP.JMP.SSE.Repository.SSEEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SSEService {

    private final SSEEmitterRepository SSERepository;   // Map 기반 리포지토리
    private final EventRepository eventRepository;  // 저장용 리포지토리
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final JWTUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;


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
    public void broadcast(EventPayload eventPayload) {

        if (eventPayload.getRole() == null) {
            throw new CustomException(ErrorCode.INVALID_ROLE); // 직접 정의한 에러코드로 대체 가능
        }

        Event savedEvent = EventMapper.toEntity(eventPayload);
        eventRepository.save(savedEvent);
        sendToClient(eventPayload.getReceiverId(), eventPayload);
    }


    // 클라이언트에게 이벤트 전송
    private void sendToClient(Long receiverId, Object data) {
        SseEmitter sseEmitter = SSERepository.findById(receiverId);
        if (sseEmitter == null) {
            // 접속중인 상대 없음: DB 저장만 하고 전송은 생략
            log.info("상대방이 실시간 접속중이지 않음: " + receiverId);
            return;
        }

        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .id(receiverId.toString())
                            .name("sse")
                            .data(data)
            );
        } catch (IOException ex) {
            SSERepository.deleteById(receiverId);
            throw new RuntimeException("SSE 전송 실패", ex);
        }
    }

    public List<EventResponse> getReceivedEvents(String token, Role role) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.NOT_AUTHENTICATED);
        }

        String accessToken = token.replace("Bearer ", "");
        String loginId = jwtUtil.getUsername(accessToken);

        Long receiverId;

        // 역할에 따라 receiverId 조회
        if (role == Role.ADMIN || role == Role.USER) {
            Account account = accountRepository.findByEmail(loginId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
            receiverId = account.getId();
        } else if (role == Role.COMPANY) {
            Company company = companyRepository.findByEmail(loginId)
                    .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));
            receiverId = company.getId();
        } else {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        List<Event> events = eventRepository.findByReceiverIdAndRole(receiverId, role);

        return events.stream()
                .map(event -> EventResponse.builder()
                        .eventId(event.getEventId())
                        .eventType(event.getEventType())
                        .message(event.getMessage())
                        .senderName(event.getSenderName())
                        .createdAt(event.getCreatedAt().toString())
                        .build()
                ).toList();
    }
}
