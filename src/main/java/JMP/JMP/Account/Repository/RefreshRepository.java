package JMP.JMP.Account.Repository;

import JMP.JMP.Account.Entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {


    Optional<RefreshEntity> findByUsername(String email);

    Boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);

    Optional<RefreshEntity> findByRefresh(String refreshToken);
}