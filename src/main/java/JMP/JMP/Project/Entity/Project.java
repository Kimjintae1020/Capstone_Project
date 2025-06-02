package JMP.JMP.Project.Entity;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Board.Dto.DtoCreateBoard;
import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Enum.RequiredSkill;
import JMP.JMP.Project.Dto.DtoCreateProject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PROJECT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long projectId;

    // 작성자 ID -> Company 담당자 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGER_ID", nullable = false)
    private Company manager;


    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "VIEW_COUNT")
    private int viewCount;          // 조회수

    @ElementCollection(targetClass = RequiredSkill.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "PROJECT_SKILLS", joinColumns = @JoinColumn(name = "PROJECT_ID"))
    @Column(name = "REQUIRED_SKILL")
    @Enumerated(EnumType.STRING)
    private List<RequiredSkill> requiredSkill;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "RECRUITCOUNT")
    private int recruitCount;           // 총 모집 인원

    @Column(name = "RECRUITDEADLINE")
    private LocalDate recruitDeadline;  // 프로젝트 공고 모집 마감일

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    public static Project createProject(Company writer, DtoCreateProject dto) {
        Project project = new Project();
        project.setManager(writer);
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setViewCount(0);
        project.setRequiredSkill(dto.getRequiredSkill());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setRecruitCount(dto.getRecruitCount());
        project.setRecruitDeadline(dto.getRecruitDeadline());
        project.setCreatedAt(LocalDate.now());
        project.setUpdatedAt(LocalDate.now());
        return project;
    }
}