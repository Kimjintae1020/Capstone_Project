package JMP.JMP.SSE.Repository;

import JMP.JMP.Enum.Role;
import JMP.JMP.SSE.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByReceiverIdAndRole(Long receiverId, Role role);
}

