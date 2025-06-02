package JMP.JMP.Project.Dto;

import JMP.JMP.Project.Entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DtoProjectRecent {

    private long projectId;
    private String title; // 프로젝트 제목
    private String description; // 프로젝트 상세내용
    private int viewCount;
    private LocalDate createdAt; // 프로젝트 등록일
    private LocalDate recruitDeadline;  // 프로젝트 공고 모집 마감일 -> 모집상태

    public DtoProjectRecent(Project project) {
        this.projectId = project.getProjectId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.viewCount = project.getViewCount();
        this.createdAt = project.getCreatedAt();
        this.recruitDeadline = project.getRecruitDeadline();
    }
}


