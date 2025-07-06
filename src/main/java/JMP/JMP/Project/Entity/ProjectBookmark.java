package JMP.JMP.Project.Entity;

import JMP.JMP.Account.Entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "PROJECT_BOOKMARK")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;    // 북마크한 날

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

}
