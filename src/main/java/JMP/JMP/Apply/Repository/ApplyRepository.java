package JMP.JMP.Apply.Repository;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Apply.Entity.Apply;
import JMP.JMP.Project.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {

    boolean existsByAccountAndProject(Account account, Project project);
}
