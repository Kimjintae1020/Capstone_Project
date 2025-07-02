package JMP.JMP.Apply.Mapper;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Apply.Entity.Apply;
import JMP.JMP.Enum.ApplyStatus;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Resume.Entity.Resume;

import java.time.LocalDate;

public class ApplyMapper {

    public static Apply toEntity(Project project, Account account, Resume resume){
        return Apply.builder()
                .project(project)
                .account(account)
                .resume(resume)
                .status(ApplyStatus.PENDING)
                .appliedAt(LocalDate.now())
                .build();
    }
}
