package JMP.JMP.Resume.Entity;

import JMP.JMP.Enum.RequiredSkill;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "RESUME_PROJECTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String githubLink;
    private LocalDate startDate; // 프로젝트 시작일
    private LocalDate endDate;   // 프로젝트 마감일

    @ElementCollection(targetClass = RequiredSkill.class, fetch = FetchType.LAZY)
    // 기본값 EAGER 이기 때문에 프로젝트 조호할 때마 모든 기술스택 조회하게 됨 -> LAZY 전략으로 변경하여 필요한 것만 조회하도록 설정
    @CollectionTable(name = "RESUME_PROJECT_TECH_STACK", joinColumns = @JoinColumn(name = "PROJECT_ID"))
    @Enumerated(EnumType.STRING)
    private List<RequiredSkill> techStack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESUME_ID")
    private Resume resume;
}
