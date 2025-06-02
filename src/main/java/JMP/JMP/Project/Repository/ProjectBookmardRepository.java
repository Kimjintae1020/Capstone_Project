package JMP.JMP.Project.Repository;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Project.Entity.ProjectBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectBookmardRepository extends JpaRepository<ProjectBookmark, Long> {

    List<ProjectBookmark> findByAccount(Account account);
}
