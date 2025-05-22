package JMP.JMP.Apply.Entity;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Enum.ApplyStatus;
import JMP.JMP.Project.Entity.Project;
import JMP.JMP.Resume.Entity.Resume;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "APPLY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPLY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESUME_ID")
    private Resume resume;

    @Enumerated(EnumType.STRING)
    private ApplyStatus status; // 지원 상태

    @Column(name = "APPLIED_AT")
    private LocalDate appliedAt;
}
