package JMP.JMP.Project.Entity;

import JMP.JMP.Company.Entity.Company;
import JMP.JMP.Enum.RequiredSkill;
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

    @ElementCollection(targetClass = RequiredSkill.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "PROJECT_SKILLS", joinColumns = @JoinColumn(name = "PROJECT_ID"))
    @Column(name = "REQUIRED_SKILL")
    @Enumerated(EnumType.STRING)
    private List<RequiredSkill> requiredSkill;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

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
}