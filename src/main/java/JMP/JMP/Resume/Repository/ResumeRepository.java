package JMP.JMP.Resume.Repository;

import JMP.JMP.Resume.Entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
