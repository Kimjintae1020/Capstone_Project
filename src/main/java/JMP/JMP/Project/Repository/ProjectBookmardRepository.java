package JMP.JMP.Project.Repository;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Entity.ProjectBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectBookmardRepository extends JpaRepository<ProjectBookmark, Long> {

    List<ProjectBookmark> findByAccount(Account account);

    Optional<Object> findByAccountAndProject(Account account, Project project);

    boolean existsByAccountAndProject(Account account, Project project);
}
