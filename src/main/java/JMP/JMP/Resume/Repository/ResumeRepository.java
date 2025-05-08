package JMP.JMP.Resume.Repository;

import JMP.JMP.Resume.Dto.DtoResumeList;
import JMP.JMP.Resume.Entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    @Query("SELECT new JMP.JMP.Resume.Dto.DtoResumeList(r.resumeId, r.title, r.intro, r.visible, r.createdAt, r.updatedAt) " +
            "FROM Resume r WHERE r.account.id = :accountId")
    List<DtoResumeList> findAllResumes(@Param("accountId") Long accountId);


    Optional<Resume> findByResumeIdAndAccountId(Long resumeId, Long accountId);
}
