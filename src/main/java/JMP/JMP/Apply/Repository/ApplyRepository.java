package JMP.JMP.Apply.Repository;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Apply.Entity.Apply;
import JMP.JMP.Project.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {

    boolean existsByAccountAndProject(Account account, Project project);

    @Query("SELECT a FROM Apply a WHERE a.project.projectId = :projectId ORDER BY a.appliedAt DESC")
    List<Apply> findRecentByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT a FROM Apply a WHERE a.account.id = :accountId")
    List<Apply> findByAccountId(@Param("accountId") Long accountId);

}
