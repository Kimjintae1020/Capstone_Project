package JMP.JMP.Project.Dto;

import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Project.Entity.ProjectBookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
public class DtoProjectScrap {

    private Long bookmarkId;          // 스크랩 고유 식별자
    private Long projectId;           // 프로젝트 ID
    private String projectTitle;      // 프로젝트 제목
    private LocalDate projectStartDate; // 프로젝트 시작일
    private LocalDate projectEndDate;   // 프로젝트 마감일
    private LocalDate recruitDeadline;  // 프로젝트 공고 모집 마감일 // 프젝 모집 마감일
    private int viewCount;             // 조회수
    private LocalDate createdAt;      // 스크랩한 시간

    public static DtoProjectScrap of(ProjectBookmark bookmark) {
        Project project = bookmark.getProject();

        return new DtoProjectScrap(
                bookmark.getId(),
                project.getProjectId(),
                project.getTitle(),
                project.getStartDate(),
                project.getRecruitDeadline(),
                project.getEndDate(),
                project.getViewCount(),
                bookmark.getCreatedAt()
        );
    }
}
