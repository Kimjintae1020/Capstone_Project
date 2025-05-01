package JMP.JMP.Resume.Entity;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Enum.RequiredSkill;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column(name = "RESUME_TILE", nullable = false)
    private String title;

    @Column(name = "RESUME_CONTENT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "RESUME_SKILL", nullable = false)
    private RequiredSkill skill;

    @Column(name = "RESUME_FILEURL", nullable = false)
    private String resumeFileUrl;

    @Column(name = "RESUME_VISIBLE", nullable = false)
    private boolean visible;

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
