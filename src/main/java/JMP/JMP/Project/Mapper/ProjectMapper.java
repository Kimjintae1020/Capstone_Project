package JMP.JMP.Project.Mapper;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Project.Dto.DtoCreateProject;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Entity.ProjectBookmark;

import java.time.LocalDate;

public class ProjectMapper {

    public static Project toEntity(Company writer, DtoCreateProject dto) {
        Project.ProjectBuilder builder = Project.builder()
                .manager(writer)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .viewCount(0)
                .requiredSkill(dto.getRequiredSkill())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .recruitCount(dto.getRecruitCount())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now());
        return builder.build();
    }

    public static ProjectBookmark addProjectBookmark(Account account, Project projectId) {
        ProjectBookmark.ProjectBookmarkBuilder builder = ProjectBookmark.builder()
                .project(projectId)
                .account(account)
                .createdAt(LocalDate.now());
        return builder.build();
    }
}
