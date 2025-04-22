package JMP.JMP.Company.Repository;

import JMP.JMP.Company.Entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRespository extends JpaRepository<Company, Long> {

    boolean existsByEmail(String email);

    boolean existsByBusinessNumber(String bussinessNumber);

    boolean existsByPhone(String phone);

    Optional<Company> findByEmail(String email);

}
