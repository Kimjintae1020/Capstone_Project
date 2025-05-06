package JMP.JMP.Resume.Entity;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Enum.DevPosition;
import JMP.JMP.Enum.RequiredSkill;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "RESUME")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESUME_ID")
    private Long resumeId;

    @Column(name = "RESUME_TITLE", nullable = false)
    private String title;               // 이력서 제목

    @Column(name = "RESUME_INTRO", nullable = false)
    private String intro;               // 이력서 한줄 소개

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "RESUME_SKILLS", joinColumns = @JoinColumn(name = "RESUME_ID"))
    @Column(name = "SKILL")
    private List<RequiredSkill> skills;     // 기술스택

    @Column(name = "RESUME_GITHUBURL", nullable = false)
    private String githuburl;               // 깃허브 링크

    @Column(name = "RESUME_VISIBLE", nullable = false)
    private boolean visible;                // 공개 범위

    @Enumerated(EnumType.STRING)
    @Column(name = "RESUME_DEVPOSITION")
    private DevPosition devposition;        // 개발 직무, 포지션

    @Column(name = "RESUME_PHOTO")
    private String photo;                   // 사진

    @Column(name = "RESUME_INTRODUCE")
    private String introduce;               // 자기소개서 500자 이내

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ResumeProject> projects;       // 프로젝트

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;                // 생성일

    @Column(name = "UPDATE_AT")
    private LocalDate updatedAt;                // 수정일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;                    // 작성자


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}