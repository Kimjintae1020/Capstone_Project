package JMP.JMP.Account.Repository;

import JMP.JMP.Account.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
}
