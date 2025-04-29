package JMP.JMP.Apply.Entity;

import JMP.JMP.Account.Entity.Account;
import JMP.JMP.Enum.ApplyStatus;
import JMP.JMP.Project.Entity.Project;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @Enumerated(EnumType.STRING)
    private ApplyStatus status; // 지원 상태

    @Column(name = "APPLIED_AT")
    private LocalDate appliedAt;
}
