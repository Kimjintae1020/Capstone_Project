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
    private String title;

    @Column(name = "RESUME_INTRO", nullable = false)
    private String intro;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "RESUME_SKILLS", joinColumns = @JoinColumn(name = "RESUME_ID"))
    @Column(name = "SKILL")
    private List<RequiredSkill> skills;

    @Column(name = "RESUME_GITHUBURL", nullable = false)
    private String githuburl;

    @Column(name = "RESUME_VISIBLE", nullable = false)
    private boolean visible;

    @Enumerated(EnumType.STRING)
    @Column(name = "RESUME_DEVPOSITION")
    private DevPosition devposition;

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;

    @Column(name = "UPDATE_AT")
    private LocalDate updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}
