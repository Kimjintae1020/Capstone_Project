package JMP.JMP.Project.Entity;

import JMP.JMP.Account.Entity.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "PROJECT_BOOKMARK")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    // 프로젝트 북마크 등록
    public static ProjectBookmark addProjectBookmark(Account account, Project projectId) {
        ProjectBookmark bookmark = new ProjectBookmark();
        bookmark.setProject(projectId);
        bookmark.setAccount(account);
        bookmark.setCreatedAt(LocalDate.now());
        return bookmark;
    }
}
