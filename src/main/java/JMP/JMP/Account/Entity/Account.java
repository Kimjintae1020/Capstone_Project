package JMP.JMP.Account.Entity;

import JMP.JMP.Enum.Education;
import JMP.JMP.Enum.Gender;
import JMP.JMP.Enum.Major;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_year", nullable = false)
    private String birthYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Major major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Education education;

    @Column(name = "created_at")
    private LocalDate createdAt;

    // 생성일 자동 설정하고 싶다면 아래에 추가해도 됨
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
