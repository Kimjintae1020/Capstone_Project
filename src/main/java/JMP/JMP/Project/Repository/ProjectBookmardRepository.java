package JMP.JMP.Project.Repository;

import JMP.JMP.Project.Entity.ProjectBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectBookmardRepository extends JpaRepository<ProjectBookmark, Long> {
}
