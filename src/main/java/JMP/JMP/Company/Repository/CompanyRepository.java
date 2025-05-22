package JMP.JMP.Company.Repository;

import JMP.JMP.Admin.Dto.DtoPendingCompanyList;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Enum.PostRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByEmail(String email);

    boolean existsByBusinessNumber(String bussinessNumber);

    boolean existsByPhone(String phone);

    Optional<Company> findByEmail(String email);

    @Query("SELECT new JMP.JMP.Admin.Dto.DtoPendingCompanyList(c.id, c.email, c.name, c.gender, c.phone, " +
            "c.businessNumber, c.position, c.companyName, c.companyLocation, c.createdAt, c.postRole) " +
            "FROM Company c WHERE c.postRole = :postRole")
    List<DtoPendingCompanyList> findAllPendingCompany(@Param("postRole") PostRole postRole);
}
